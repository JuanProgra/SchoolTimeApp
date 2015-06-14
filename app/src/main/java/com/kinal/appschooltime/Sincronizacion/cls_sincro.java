package com.kinal.appschooltime.Sincronizacion;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.kinal.appschooltime.BaseDatos.BDD_sqlite;
import com.kinal.appschooltime.beans.Horario;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

/**
 * Created by Godinez Miranda on 13/06/2015.
 */
public class cls_sincro {
    private static final int TimeOut = 10000;
    private int timer = 10000;
    private static String URL = "http://fkinal2015-001-site1.smarterasp.net/WebService1.asmx";
    private static String NAMESPACE = "http://tempuri.org/";
    public static String mensaje_proceso = "";
    public static String status = "ok";

    public String GetUsuarioValido(Context cx, String user, String password) {
        BDD_sqlite usdbh = new BDD_sqlite(cx);
        SQLiteDatabase db = usdbh.getWritableDatabase();
        String estado = "0";
        String nombre = "";
        try {
            String METHOD_NAME = "Login";
            String SOAP_ACTION = "http://tempuri.org/Login";
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("user", user);
            request.addProperty("password", password);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transporte = new HttpTransportSE(URL, TimeOut);
            transporte.call(SOAP_ACTION, envelope);
            envelope.setOutputSoapObject(request);

            SoapObject result_web = (SoapObject) envelope.bodyIn;
            nombre = result_web.getProperty(0).toString();

            if (nombre.length() > 0) {
                if (nombre.equals("0")) {
                    estado = "0";
                } else {
                    ContentValues contentValues = new ContentValues();

                    String sql1 = "DELETE FROM USUARIO";
                    db.execSQL(sql1);
                    db.close();

                    db = usdbh.getReadableDatabase();

                    contentValues.put("NOMBREAP", nombre);
                    contentValues.put("USUARIO", user);

                    db.insert("USUARIO", null, contentValues);
                    db.close();
                    estado = "1";
                }
            } else {
                estado = "0";
            }
            return estado;
        } catch (Exception e) {
            estado = "Error Login:" + e.getMessage().toString();
            mensaje_proceso = estado;
            return estado;
        }
    }

    public String GetCodigo(String user) {
        String estado = "0";
        String nombre = "";
        try {
            String METHOD_NAME = "Get_Grupo";
            String SOAP_ACTION = "http://tempuri.org/Get_Grupo";
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("usuairo", user);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transporte = new HttpTransportSE(URL, TimeOut);
            transporte.call(SOAP_ACTION, envelope);
            envelope.setOutputSoapObject(request);

            SoapObject result_web = (SoapObject) envelope.bodyIn;
            nombre = result_web.getProperty(0).toString();

            if (nombre.length() > 0) {
                estado = nombre;
            } else {
                estado = "0";
            }
            return estado;
        } catch (Exception e) {
            estado = "Error Login:" + e.getMessage().toString();
            mensaje_proceso = estado;
            return estado;
        }
    }

    public String GetHorario(Context cx, String codigo) {
        String result = "0";
        BDD_sqlite usdbh = new BDD_sqlite(cx);
        SQLiteDatabase db = usdbh.getWritableDatabase();
        try {
            String sql1 = "DELETE FROM HORARIO";
            db.execSQL(sql1);
            db.close();

            ArrayList<Horario> horarioArrayList = new ArrayList<>();
            Horario horario = new Horario();
            String METHOD_NAME = "Get_horario";
            String SOAP_ACTION = "http://tempuri.org/Get_horario";
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("codigo", codigo);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transporte = new HttpTransportSE(URL, TimeOut);
            transporte.call(SOAP_ACTION, envelope);
            envelope.setOutputSoapObject(request);
            SoapObject resSoap = (SoapObject) envelope.getResponse();

            ContentValues horarioConte = new ContentValues();

            int valida = 0;
            try {
                for (int i = 0; i < resSoap.getPropertyCount(); i++) {
                    SoapObject ic = (SoapObject) resSoap.getProperty(i);

                    db = usdbh.getReadableDatabase();

                    horarioConte.put("MATERIA", ic.getProperty(0).toString());
                    horarioConte.put("SALON", ic.getProperty(1).toString());
                    horarioConte.put("HORA", ic.getProperty(2).toString());
                    horarioConte.put("DIA", ic.getProperty(3).toString());

                    db.insert("HORARIO", null, horarioConte);
                    db.close();

                    result = "1";
                }
            } catch (Exception e) {
                result = "Error horario:" + e.getMessage().toString();
                mensaje_proceso = result;
                db.close();
                return result;
            }
            return result;
        } catch (Exception e) {
            result = "Error horario:" + e.getMessage().toString();
            mensaje_proceso = result;
            return result;
        }
    }
    public String GetMateria(Context cx, String codigo) {
        String result = "0";
        BDD_sqlite usdbh = new BDD_sqlite(cx);
        SQLiteDatabase db = usdbh.getWritableDatabase();
        try {
            String sql1 = "DELETE FROM MATERIA";
            db.execSQL(sql1);
            db.close();

            ArrayList<Horario> horarioArrayList = new ArrayList<>();
            Horario horario = new Horario();
            String METHOD_NAME = "Get_materia";
            String SOAP_ACTION = "http://tempuri.org/Get_materia";
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("codigo", codigo);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transporte = new HttpTransportSE(URL, TimeOut);
            transporte.call(SOAP_ACTION, envelope);
            envelope.setOutputSoapObject(request);
            SoapObject resSoap = (SoapObject) envelope.getResponse();

            ContentValues horarioConte = new ContentValues();

            int valida = 0;
            try {
                for (int i = 0; i < resSoap.getPropertyCount(); i++) {
                    SoapObject ic = (SoapObject) resSoap.getProperty(i);

                    db = usdbh.getReadableDatabase();

                    horarioConte.put("NOMBRE", ic.getProperty(0).toString());

                    db.insert("MATERIA", null, horarioConte);
                    db.close();

                    result = "1";
                }
            } catch (Exception e) {
                result = "Error horario:" + e.getMessage().toString();
                mensaje_proceso = result;
                db.close();
                return result;
            }
            return result;
        } catch (Exception e) {
            result = "Error horario:" + e.getMessage().toString();
            mensaje_proceso = result;
            return result;
        }
    }
    public String GetTarea(Context cx, String materia) {
        String result = "0";
        BDD_sqlite usdbh = new BDD_sqlite(cx);
        SQLiteDatabase db = usdbh.getWritableDatabase();
        try {
            String sql1 = "DELETE FROM TAREA WHERE NOMBRETARE='"+materia+"'";
            db.execSQL(sql1);
            db.close();

            ArrayList<Horario> horarioArrayList = new ArrayList<>();
            Horario horario = new Horario();
            String METHOD_NAME = "Get_tareas";
            String SOAP_ACTION = "http://tempuri.org/Get_tareas";
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("materia", materia);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transporte = new HttpTransportSE(URL, TimeOut);
            transporte.call(SOAP_ACTION, envelope);
            envelope.setOutputSoapObject(request);
            SoapObject resSoap = (SoapObject) envelope.getResponse();

            ContentValues tareaConte = new ContentValues();

            int valida = 0;
            try {
                for (int i = 0; i < resSoap.getPropertyCount(); i++) {
                    SoapObject ic = (SoapObject) resSoap.getProperty(i);

                    db = usdbh.getReadableDatabase();

                    tareaConte.put("NOMBRETARE", ic.getProperty(0).toString());
                    tareaConte.put("NOMBRE", ic.getProperty(1).toString());
                    tareaConte.put("DETALLE", ic.getProperty(2).toString());
                    tareaConte.put("FECHA", ic.getProperty(3).toString());
                    tareaConte.put("PUNTEO", ic.getProperty(4).toString());

                    db.insert("TAREA", null, tareaConte);
                    db.close();

                    result = "1";
                }
            } catch (Exception e) {
                result = "Error horario:" + e.getMessage().toString();
                mensaje_proceso = result;
                db.close();
                return result;
            }
            return result;
        } catch (Exception e) {
            result = "Error horario:" + e.getMessage().toString();
            mensaje_proceso = result;
            return result;
        }
    }
}
