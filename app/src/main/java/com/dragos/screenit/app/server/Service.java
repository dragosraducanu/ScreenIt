package com.dragos.screenit.app.server;

import android.util.Log;

import com.dragos.androidfilepicker.library.core.ImageSize;

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
    private String mId;
    private String mBrowserId;
    private ImageSize mBrowserWindowSize;

    public boolean connect(String browserId) {
        mBrowserId = browserId;
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
            json.put("type", "android");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //send the client type to the server.
        mSocket.emit(NodeEvent.CLIENT_TYPE, json);
        //also ask the server to generate and send an id for this device.
        mSocket.emit(NodeEvent.REQUEST_ID);
    }

    private void getIdFromServer(JSONObject _data){
        try {
            mId = _data.getString("id");
        } catch (JSONException e){
            e.printStackTrace();
        }
        Log.w("service", "id from server is: " + mId);

        //now would be a good time to ask the server to pair the clients
        pairClients();
    }

    public void sendImagePathToServer(String imagePath){
        JSONObject json = new JSONObject();
        try {
            json.put("img", imagePath);
        } catch (JSONException e){
            e.printStackTrace();
        }
        mSocket.emit(NodeEvent.PUSH_IMAGE_TO_BROWSER, json);
    }

    private void pairClients() {
        JSONObject json = new JSONObject();
        try {
            json.put("androidID", mId);
            json.put("browserID", mBrowserId);
        } catch (JSONException e) {
            e.printStackTrace();
            //probably a good place to show the user an error message
            return;
        }
        mSocket.emit(NodeEvent.PAIR_CLIENTS, json);

    }
    private void getBrowserSize(JSONObject _data){
        try {
            mBrowserWindowSize = new ImageSize(_data.getInt("width"), _data.getInt("height"));
        }catch (JSONException e){
            e.printStackTrace();
        }
        Log.w("service", "W: " + mBrowserWindowSize.getWidth());
    }


    @Override
    public void onDisconnect() {
        Log.e("service", "onDisconnect");
    }

    @Override
    public void onConnect() {
        Log.e("service", "onConnect");
    }

    @Override
    public void onMessage(String s, IOAcknowledge ioAcknowledge) {
        Log.e("service", "onMessage");
    }

    @Override
    public void onMessage(JSONObject jsonObject, IOAcknowledge ioAcknowledge) {

    }

    @Override
    public void on(String s, IOAcknowledge ioAcknowledge, Object... objects) {
        Log.e("service", "event: " + s);
        if(s.equals(NodeEvent.REQUEST_CLIENT_TYPE)) {
            sendClientTypeToServer();
        } else if(s.equals(NodeEvent.ID_GENERATED)) {
            getIdFromServer((JSONObject) objects[0]);
        } else if(s.equals(NodeEvent.SEND_BROWSER_SIZE)) {
            getBrowserSize((JSONObject) objects[0]);
        }
    }


    @Override
    public void onError(SocketIOException e) {
        Log.e("service", "onError");
        e.printStackTrace();
    }

    public String getId(){
        return this.mId;
    }

    public ImageSize getBrowserWindowSize(){
        return this.mBrowserWindowSize;
    }
}
