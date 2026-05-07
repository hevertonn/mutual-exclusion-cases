import java.util.HashMap;

enum Task {
  TASK0(0),
  TASK1(1);

  private int id;

  private Task(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }
}

enum MutualExclusionType {
  LOCK_VARIABLE,
  STRICT_ALTERNATION,
  PETERSON_SOLUTION,
  NONE
}

public class Main {
  // ######################################################
  // ###################### SETTINGS ######################
  // ######################################################

  // Algorithm selection.
  public static final MutualExclusionType mutualExclusionType = MutualExclusionType.NONE;

  // Set how many times the for loop will repeat.
  public static final int numberOfIterations = 100;

  // Set to true to sleep for 1 millisecond in the critical region.
  public static final boolean sleepInCriticalRegion = false;

  // #######################################################
  // #######################################################
  // #######################################################

  // Shared variable.
  public static int sharedData;

  // Mutual exclusion control variables.
  public static boolean lockVariable = false;
  public static int turn = 0;
  public static boolean[] interested = new boolean[2];

  public static void main(String[] args) throws InterruptedException {
    HashMap<Integer, Integer> occurrences = new HashMap<>();

    for (int i = 0; i < numberOfIterations; i++) {
      Task0 task0 = new Task0();
      Task1 task1 = new Task1();

      sharedData = 2;

      task0.start();
      task1.start();

      task0.join();
      task1.join();

      occurrences.putIfAbsent(sharedData, 0);
      occurrences.put(sharedData, occurrences.get(sharedData) + 1);
    }

    for (int k : occurrences.keySet()) {
      System.out.printf("Key: %d | Occurrences: %d%n", k, occurrences.get(k));
    }
  }

  public static void enterRegion(Task task) {
    switch (mutualExclusionType) {
      case LOCK_VARIABLE:
        enterWithLockVariable();
        break;
      case STRICT_ALTERNATION:
        enterWithStrictAlternation(task);
        break;
      case PETERSON_SOLUTION:
        enterWithPetersonMethod(task);
        break;
      default:
        break;
    }
  }

  public static void leaveRegion(Task task) {
    switch (mutualExclusionType) {
      case LOCK_VARIABLE:
        leaveWithLockVariable();
        break;
      case STRICT_ALTERNATION:
        leaveWithStrictAlternation(task);
        break;
      case PETERSON_SOLUTION:
        leaveWithPetersonMethod(task);
        break;
      default:
        break;
    }
  }

  public static void enterWithLockVariable() {
    while (lockVariable) {
    }
    lockVariable = true;
  }

  public static void leaveWithLockVariable() {
    lockVariable = false;
  }

  public static void enterWithStrictAlternation(Task task) {
    while (task.getId() != turn) {
    }
  }

  public static void leaveWithStrictAlternation(Task task) {
    if (task.getId() == turn) {
      switch (task) {
        case TASK0:
          turn = Task.TASK1.getId();
          break;
        case TASK1:
          turn = Task.TASK0.getId();
          break;
      }
    }
  }

  public static void enterWithPetersonMethod(Task task) {
    int other = 1 - task.getId();
    interested[task.getId()] = true;
    turn = task.getId();

    while (turn == task.getId() && interested[other] == true) {
    }
  }

  public static void leaveWithPetersonMethod(Task task) {
    interested[task.getId()] = false;
  }
}

class Task0 extends Thread {
  private Task thisTask = Task.TASK0;

  @Override
  public void run() {
    Main.enterRegion(thisTask);
    Main.sharedData++;

    if (Main.sleepInCriticalRegion) {
      try {
        Thread.sleep(1);
      } catch (InterruptedException ie) {
        System.out.println(ie.toString());
        return;
      }
    }

    Main.leaveRegion(thisTask);
  }
}

class Task1 extends Thread {
  private Task thisTask = Task.TASK1;

  @Override
  public void run() {
    Main.enterRegion(thisTask);
    Main.sharedData--;

    if (Main.sleepInCriticalRegion) {
      try {
        Thread.sleep(1);
      } catch (InterruptedException ie) {
        System.out.println(ie.toString());
        return;
      }
    }

    Main.leaveRegion(thisTask);
  }
}
