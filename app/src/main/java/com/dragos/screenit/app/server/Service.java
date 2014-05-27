package com.dragos.screenit.app.server;

import android.util.Log;

import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

/**
 * Created by dragos on 27.05.2014.
 */
public class Service implements IOCallback{



    private static Service instance = null;
    protected Service(){

    }
    public static Service getInstance() {
        if(instance == null) {
            instance = new Service();
        }
        return instance;
    }

    private SocketIO mSocket;
    private int mId;

    public boolean connect() {

        try {
            mSocket = new SocketIO();
            mSocket.connect("http://46.214.74.147", this);
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void sendClientTypeToServer() {
        JSONObject json = new JSONObject();
        try {
            json.putOpt("type", "android");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit(NodeEvent.CLIENT_TYPE, json);
        mSocket.emit(NodeEvent.REQUEST_ID);
    }

    private void getIdFromServer(JsonElement _data){
        Log.w("service", "id from server is: " + _data.getAsInt());
    }

    @Override
    public void onDisconnect() {

    }

    @Override
    public void onConnect() {

    }

    @Override
    public void onMessage(String s, IOAcknowledge ioAcknowledge) {

    }

    @Override
    public void onMessage(JsonElement jsonElement, IOAcknowledge ioAcknowledge) {

    }

    @Override
    public void on(String s, IOAcknowledge ioAcknowledge, JsonElement... jsonElements) {
        if(s.equals(NodeEvent.REQUEST_CLIENT_TYPE)) {
           sendClientTypeToServer();
        } else if(s.equals(NodeEvent.ID_GENERATED)) {
            getIdFromServer(jsonElements[0]);
        }
    }

    @Override
    public void onError(SocketIOException e) {

    }

    public int getId(){
        return this.mId;
    }
}
