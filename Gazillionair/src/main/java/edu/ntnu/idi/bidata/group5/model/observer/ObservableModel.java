package edu.ntnu.idi.bidata.group5.model.observer;

/**
 * Contract for models that publish state changes to observers.
 */
public interface ObservableModel {

  /**
   * Adds an observer that should be notified on state changes.
   *
   * @param observer observer instance
   */
  void addObserver(ModelObserver observer);

  /**
   * Removes a previously registered observer.
   *
   * @param observer observer instance
   */
  void removeObserver(ModelObserver observer);

  /**
   * Notifies all observers that model state has changed.
   */
  void notifyObservers();
}
