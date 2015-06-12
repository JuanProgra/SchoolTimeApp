package Sincro;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import BaseDatos.BDD_sqlite;
import Beans.Horario;

/**
 * Created by Godinez Miranda on 10/06/2015.
 */
public class cls_sincro {
    private static final int TimeOut = 10000;
    private int timer = 10000;
    private static String URL = "http://fkinal2015-001-site1.smarterasp.net/WebService1.asmx";
    private static String NAMESPACE = "http://tempuri.org/";
    public static String mensaje_proceso = "";
    public static String status = "ok";

    public String idColegio_, idRuta_ = "0";

    private SimpleDateFormat dateFormat;

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
            HttpTransportSE transporte = new HttpTransportSE(URL,TimeOut);
            transporte.call(SOAP_ACTION, envelope);
            envelope.setOutputSoapObject(request);

            SoapObject result_web = (SoapObject)envelope.bodyIn;
            nombre = result_web.getProperty(0).toString();

            if (nombre.length() > 0){
                if (nombre.equals("0")){
                    estado = "0";
                }else{
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
            }else {
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
            HttpTransportSE transporte = new HttpTransportSE(URL,TimeOut);
            transporte.call(SOAP_ACTION, envelope);
            envelope.setOutputSoapObject(request);

            SoapObject result_web = (SoapObject)envelope.bodyIn;
            nombre = result_web.getProperty(0).toString();

            if (nombre.length() > 0){
                if (nombre.equals("0")){
                    estado = "0";
                }else{
                    estado = nombre;
                }
            }else {
                estado = "0";
            }
            return estado;
        } catch (Exception e) {
            estado = "Error codigo:" + e.getMessage().toString();
            mensaje_proceso = estado;
            return estado;
        }
    }
    public String GetHorario(Context cx, String codigo, String time){
        String result="0";
        BDD_sqlite usdbh = new BDD_sqlite(cx);
        SQLiteDatabase db = usdbh.getWritableDatabase();
        try
        {
            ArrayList<Horario> horarioArrayList = new ArrayList<>();
            Horario horario = new Horario();
            String METHOD_NAME="Get_horario";
            String SOAP_ACTION = "http://tempuri.org/Get_horario";
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("codigo", "PE6BV");
            request.addProperty("time", "12/06/2015");

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transporte = new HttpTransportSE(URL,TimeOut);
            transporte.call(SOAP_ACTION, envelope);
            envelope.setOutputSoapObject(request);
            SoapObject resSoap =(SoapObject)envelope.getResponse();

            ContentValues horarioConte = new ContentValues();

            int valida =0;
            for (int i = 0; i < resSoap.getPropertyCount(); i++)
            {
                SoapObject ic = (SoapObject)resSoap.getProperty(i);

                horario.setMateria(ic.getProperty(0).toString());
                horario.setSalon(ic.getProperty(1).toString());
                horario.setHora(ic.getProperty(2).toString());
                horario.setDia(ic.getProperty(3).toString());
                horarioArrayList.add(horario);
                valida++;
            }

            try {

                if (valida > 0){
                    db = usdbh.getReadableDatabase();

                    horarioConte.put("HORA", horario.getHora().toString());
                    horarioConte.put("SALON", horario.getSalon().toString());
                    horarioConte.put("MATERIA", horario.getMateria().toString());
                    horarioConte.put("DIA", horario.getDia().toString());

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
        }
        catch (Exception e)
        {
            result = "Error horario:" + e.getMessage().toString();
            mensaje_proceso = result;
            return result;
        }
    }
}
