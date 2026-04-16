package edu.ntnu.idi.bidata.group5.model.observer;

/**
 * Observer callback for model state changes.
 */
@FunctionalInterface
public interface ModelObserver {

  /**
   * Called after observable model state has changed.
   */
  void onModelChanged();
}
