package isen.isensays20;

import android.content.Context;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Stack;

/**
 * Created by Ilya on 07.02.2017.
 */

public class MyObservable extends Observable {

    private static List<Observer> observers;

    public MyObservable(Observer observer){
        addObserver(observer);
    }

    static{
        observers = new LinkedList<>();
    }

    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void deleteObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(Object arg){

        for(Observer observer : observers){
            observer.update(this,arg);
        }

    }


}
