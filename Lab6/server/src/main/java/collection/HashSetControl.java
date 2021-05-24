package collection;

import file.StudyGroupDAO;
import locale.ServerBundle;
import response.Creator;
import studygroup.StudyGroup;
import studygroup.StudygroupFormatter;
import studygroup.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class HashSetControl implements CollectionControl {

    private HashSet<StudyGroup> studyGroupHashSet;

    private final Creator creator;

    private final Lock lock = new ReentrantLock();

    private final StudyGroupDAO studyGroupDAO;

    private final LocalDate dateOfInit = LocalDate.now();

    public HashSetControl(Creator creator, StudyGroupDAO studyGroupDAO) {
        this.studyGroupDAO = studyGroupDAO;
        studyGroupHashSet = new HashSet<>();
        this.creator = creator;
        init();
    }

    private void init() {
        studyGroupHashSet = new HashSet<>(studyGroupDAO.getCollection());
    }

    public void addStudyGroupById(StudyGroup studyGroup, User user) {
        lock();
        studyGroup.setUsername(user.getLogin());
        studyGroupDAO.insertGroup(studyGroup);
        studyGroupHashSet.add(studyGroup);
        unlock();
    }

    public void addStudyGroupByIdIfMax(StudyGroup studyGroup, User user) {
        lock();
        studyGroup.setUsername(user.getLogin());
        if (studyGroupHashSet.isEmpty() || studyGroup.compareTo(studyGroupHashSet.stream().max(Comparator.comparingLong(StudyGroup::getStudentsCount)).get()) > 0) {
            studyGroupDAO.insertGroup(studyGroup);
            studyGroupHashSet.add(studyGroup);
        }
        unlock();
    }

    public void addStudyGroup(StudyGroup studyGroup) {
        studyGroupHashSet.add(studyGroup);
    }

    public void addStudyGroups(Collection<StudyGroup> collection) {
        studyGroupHashSet.addAll(collection);
    }

    public HashSet<StudyGroup> getStudyGroupHashSet() {
        return studyGroupHashSet;
    }

    public void info() {
        lock();
        creator.addToMsg(String.format(ServerBundle.getString("collection.info"), studyGroupHashSet.getClass().getName(),
                dateOfInit.toString(), studyGroupHashSet.size()));
        unlock();
    }

    public void clear(User user) {
        lock();
        studyGroupHashSet.stream().filter(x -> x.getUsername().equals(user.getLogin())).forEach(studyGroupDAO::deleteGroup);
        studyGroupHashSet = studyGroupHashSet.stream().filter(x -> !x.getUsername().equals(user.getLogin())).collect(Collectors.toCollection(HashSet::new));
        unlock();
    }

    public void show() {
        lock();
        StudygroupFormatter formatter = new StudygroupFormatter();
        studyGroupHashSet.stream()
                .sorted()
                .forEach(x -> creator.addToMsg(formatter.formatStudyGroup(x)));
        unlock();
    }

    public void printAscending() {
        lock();
        StudygroupFormatter formatter = new StudygroupFormatter();
        studyGroupHashSet.stream().sorted().forEach(x -> creator.addToMsg(formatter.formatStudyGroup(x)));
        unlock();
    }

    public void printUniqueExpelledStudents() {
        lock();
        studyGroupHashSet.stream()
                .flatMapToLong(x -> LongStream.of(x.getExpelledStudents()))
                .distinct()
                .forEach(x -> creator.addToMsg(String.valueOf(x)));
        unlock();
    }

    public void printFieldDescendingFormOfEducation() {
        lock();
        studyGroupHashSet.stream()
                .sorted(Comparator.comparingInt(o -> ((StudyGroup) o).getFormOfEducation().ordinal()).reversed())
                .forEach(x -> creator.addToMsg(String.valueOf(x.getFormOfEducation().ordinal())));
        unlock();
    }

    public void removeById(int id, User user) {
        lock();
        if(studyGroupHashSet.stream().anyMatch(x -> x.getId() == id && x.getUsername().equals(user.getLogin())))
            studyGroupDAO.deleteGroupByID(id);
        try {
            studyGroupHashSet.remove(studyGroupHashSet.stream().filter(x -> x.getId() == id && x.getUsername().equals(user.getLogin())).findFirst().get());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(ServerBundle.getString("exception.nosuch"));
        }
        unlock();
    }


    public void removeGreater(StudyGroup studyGroup, User user) {
        lock();
        studyGroup.setUsername(user.getLogin());
        studyGroupHashSet.stream()
                .filter(x -> studyGroup.compareTo(x) <= 0 && studyGroup.getUsername().equals(x.getUsername()))
                .forEach(studyGroupDAO::deleteGroup);
        studyGroupHashSet = studyGroupHashSet.stream()
                .filter(x -> studyGroup.compareTo(x) > 0 || !studyGroup.getUsername().equals(x.getUsername()))
                .collect(Collectors.toCollection(HashSet::new));
        unlock();
    }

    public void removeLower(StudyGroup studyGroup, User user) {
        lock();
        studyGroup.setUsername(user.getLogin());
        studyGroupHashSet.stream()
                .filter(x -> studyGroup.compareTo(x) >= 0 && studyGroup.getUsername().equals(x.getUsername()))
                .forEach(studyGroupDAO::deleteGroup);
        studyGroupHashSet = studyGroupHashSet.stream()
                .filter(x -> studyGroup.compareTo(x) < 0 || !studyGroup.getUsername().equals(x.getUsername()))
                .collect(Collectors.toCollection(HashSet::new));
        unlock();
    }



    public void updateId(int id, StudyGroup studyGroup, User user)
            throws NumberFormatException{
        lock();
        studyGroup.setId(id);
        studyGroup.setUsername(user.getLogin());
        if(studyGroupHashSet.stream().anyMatch(x -> x.getId() == id && x.getUsername().equals(studyGroup.getUsername()))) {
            studyGroupDAO.updateGroup(studyGroup);
            studyGroupHashSet.remove(studyGroupHashSet.stream().filter(x -> x.getId() == id && x.getUsername().equals(studyGroup.getUsername())).findFirst().get());
            studyGroupHashSet.add(studyGroup);
        }
        unlock();
    }

    private void lock() {
        try {
            while (!lock.tryLock()) {
                lock.wait();
            }
        } catch (InterruptedException ignored) {

        }
    }

    private void unlock() {
        lock.unlock();
    }
}
