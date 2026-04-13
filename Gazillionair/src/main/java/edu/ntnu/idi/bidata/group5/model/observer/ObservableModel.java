package edu.ntnu.idi.bidata.group5.model.observer;

public interface ObservableModel {

  void addObserver(ModelObserver observer);

  void removeObserver(ModelObserver observer);

  void notifyObservers();
}
