package com.example.tinyowlsampleapp.database;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.example.tinyowlsampleapp.RestaurantsApplication;
import com.example.tinyowlsampleapp.interfaces.Command;

import java.util.Stack;

/**
 * Created by vihaan on 27/12/14.
 */
public class CartCommandManager
//        extends Stack<CartCommand>{
{
    private Stack<Command> mUndoCommandStack;
    private Stack<Command> mRedoCommandStack;

    private static CartCommandManager cartCommandManager;

    private Context mContext;

    private CartCommandManager()
    {
        mUndoCommandStack = new Stack();
        mRedoCommandStack = new Stack();

        setContext(RestaurantsApplication.getAppContext());
    }

    public static CartCommandManager  getInstance()
    {
        if(cartCommandManager == null)
        {
            cartCommandManager = new CartCommandManager();

        }

        return cartCommandManager;
    }

    private void setContext(Context context)
    {
        mContext = context;
    }

    private void sendCartUpdateBroadCast()
    {
        Intent intent = new Intent("cart_update");
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }


    public Command pushInUndoStack(Command cartCommand)
    {
        mUndoCommandStack.push(cartCommand);
        mRedoCommandStack.clear();
        return cartCommand;
    }

    public boolean canUndo()
    {
        return !mUndoCommandStack.isEmpty();
    }

    public boolean canRedo()
    {
        return !mRedoCommandStack.isEmpty();
    }


    public void undo()
    {
        if(!canUndo())
        {
            throw new IllegalStateException("Can't undo");
        }


        Command c = (Command) mUndoCommandStack.pop();
        mRedoCommandStack.push(c);
        c.undo();
        sendCartUpdateBroadCast();
    }

    public void redo()
    {
        if(!canRedo())
        {
            throw new IllegalStateException("Can't redo");
        }

        Command c = mRedoCommandStack.pop();
        mUndoCommandStack.push(c);
        c.redo();
        sendCartUpdateBroadCast();
    }
}
