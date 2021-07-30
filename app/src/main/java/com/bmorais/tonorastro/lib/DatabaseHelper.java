package com.bmorais.tonorastro.lib;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.bmorais.tonorastro.PrincipalActivity;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * 
 * @author BRUNO SANTOS MORAIS
 *
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    //Constantes para atualizacao do banco de dados
    public static final String UPLOAD_DATABASE = "UPLOAD_DATABASE";
    public static final String COMPLETE_UPDATE = "COMPLETE_UPDATE";

    public static final String DATABASE_NAME = "tonorastro.sqlite";
	private static final int DATABASE_VERSION = 2; //Deve ser igual a do arquivo assets/DcOcorrenciasDB
	private static final String TAG = "DatabaseHelper";
    private static final String DB_PATH_SUFFIX = "/databases/";

    private Context context;
    private SharedPreferences vrShared = null;
    private SharedPreferences.Editor vrEditor = null;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        this.vrShared = context.getSharedPreferences("persiste", PrincipalActivity.MODE_PRIVATE);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Database version: " + db.getVersion());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + "to " + newVersion + ", which will destroy all old data");
        //Reseta o status das variaveis de persistencia indicando ao sistema
        //que este deve realizar atualizacao da base de dados
        resetStatusBD();
        //Copia a base de dados original, resetando o banco de dados
		onConfigure(db);
	}

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        //Reseta a base de dados
        uploadFileDatadabase();
    }

    /**
     * Copia o arquivo do banco de dados para o diretorio do app
     */
    private void uploadFileDatadabase() {
        if(!vrShared.getBoolean(UPLOAD_DATABASE, false)) {
            try {
                createDataBase();
                //Recupera o editor da variavel de persistencia
                vrEditor = vrShared.edit();
                //Informa ao sistema que foi realizado o upload do arquivo de banco de dados
                vrEditor.putBoolean(UPLOAD_DATABASE, true);
                //Aplica as alteracoes
                vrEditor.apply();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Copia o banco de dados
     * @throws IOException
     */
    public void createDataBase() throws IOException {
        boolean mDataBaseExist = checkDataBase();
        if(!mDataBaseExist) {
            //Deleta a base de dados atual
            context.deleteDatabase(DATABASE_NAME);
            try {
                //Copia a nova base de dados
                copyDataBase();
                Log.e(TAG, "createDatabase database created");
            } catch (IOException e) {
                throw new Error("ErrorCopyingDataBase");
            }
        } else {
            try {
                //Copia a nova base de dados
                copyDataBase();
                Log.e(TAG, "createDatabase database created");
            } catch (IOException e) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    /**
     * Reseta o status das variaveis de persistencia
     */
    private void resetStatusBD() {
        vrEditor = vrShared.edit();
        //Informa ao sistema que foi deve fazer upload do banco de dados
        vrEditor.remove(UPLOAD_DATABASE);
        //Informa ao sistema que esteve deve fazer atualizacao da base de dados com o servidor
        vrEditor.remove(COMPLETE_UPDATE);
        //Aplica as alteracoes
        vrEditor.apply();
    }

    /**
     * Copia o arquivo do banco do diretorio assets para a pasta data/data/databases
     * @throws IOException
     */
    public void copyDataBase() throws IOException {
        try {
            File file = new File(context.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!file.exists()) {
                file.mkdir();
            }
            InputStream mInputStream = context.getAssets().open(DATABASE_NAME);
            OutputStream mOutputStream = new FileOutputStream(getDatabasePath());
            byte[] buffer = new byte[1024];
            int length;
            while ((length = mInputStream.read(buffer)) > 0) {
                mOutputStream.write(buffer, 0, length);
            }
            mOutputStream.flush();
            mOutputStream.close();
            mInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return String com o nome do arquivo SQLite
     */
    private String getDatabasePath() {
        return context.getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
    }

    /**
     * Verifica se o arquivo do banco de dados existe
     * @return boolean
     */
    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH_SUFFIX + DATABASE_NAME);
        return dbFile.exists();
    }


    public void backup(String outFileName) {

        //database path
        final String inFileName = context.getDatabasePath(DATABASE_NAME).toString();

        try {

            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);

            // Open the empty db as the output stream
            OutputStream output = new FileOutputStream(outFileName);

            // Transfer bytes from the input file to the output file
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            // Close the streams
            output.flush();
            output.close();
            fis.close();

            //Toast.makeText(context, "Backup Local Salvo", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(context, "Unable to backup database. Retry", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void importDB(String inFileName) {

        final String outFileName = context.getDatabasePath(DATABASE_NAME).toString();

        try {

            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);

            // Open the empty db as the output stream
            OutputStream output = new FileOutputStream(outFileName);

            // Transfer bytes from the input file to the output file
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            // Close the streams
            output.flush();
            output.close();
            fis.close();

            Toast.makeText(context, "Backup restaurado", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(context, "Unable to import database. Retry", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public static int getDatabaseVersion() {
        return DATABASE_VERSION;
    }
}
