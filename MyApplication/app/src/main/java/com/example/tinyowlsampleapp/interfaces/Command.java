package com.example.tinyowlsampleapp.interfaces;

import com.example.tinyowlsampleapp.beans.Item;

/**
 * Created by vihaan on 26/12/14.
 */
public interface Command {
    public void undo();
    public void redo();
}
