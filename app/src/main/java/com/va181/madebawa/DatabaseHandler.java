package com.va181.madebawa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DatabaseHandler extends SQLiteOpenHelper {

    private final static int DATABASE_VERSION = 2;
    private final static String DATABASE_NAME = "db_film";
    private final static String TABLE_FILM = "tb_film";
    private final static String KEY_ID_FILM = "ID_Film";
    private final static String KEY_JUDUL = "Judul_Film";
    private final static String KEY_TGL = "Tanggal";
    private final static String KEY_GAMBAR = "Poster";
    private final static String KEY_GENRE = "Genre_Film";
    private final static String KEY_SUTRADARA = "Sutradara_Film";
    private final static String KEY_PEMERAN = "Pemeran";
    private final static String KEY_SINOPSIS = "Sinopsis_Film";
    private SimpleDateFormat sdFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private Context context;

    public DatabaseHandler(Context ctx){
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = ctx;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_FILM = "CREATE TABLE " + TABLE_FILM
                + "(" + KEY_ID_FILM + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_JUDUL + " TEXT, " + KEY_TGL + " DATE, "
                + KEY_GAMBAR + " TEXT, " + KEY_GENRE + " TEXT, "
                + KEY_SUTRADARA + " TEXT, " + KEY_PEMERAN + " TEXT, "
                + KEY_SINOPSIS + " TEXT);";

        db.execSQL(CREATE_TABLE_FILM);
        inisialisasiFilmAwal(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_FILM;
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    public void tambahFilm(Film dataFilm){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_JUDUL, dataFilm.getJudul());
        cv.put(KEY_TGL, sdFormat.format(dataFilm.getTanggal()));
        cv.put(KEY_GAMBAR, dataFilm.getGambar());
        cv.put(KEY_GENRE, dataFilm.getGenre());
        cv.put(KEY_SUTRADARA, dataFilm.getSutradara());
        cv.put(KEY_PEMERAN, dataFilm.getPemeran());
        cv.put(KEY_SINOPSIS, dataFilm.getSinopsis());

        db.insert(TABLE_FILM, null, cv);
        db.close();
    }

    public void tambahFilm(Film dataFilm, SQLiteDatabase db){
        ContentValues cv = new ContentValues();

        cv.put(KEY_JUDUL, dataFilm.getJudul());
        cv.put(KEY_TGL, sdFormat.format(dataFilm.getTanggal()));
        cv.put(KEY_GAMBAR, dataFilm.getGambar());
        cv.put(KEY_GENRE, dataFilm.getGenre());
        cv.put(KEY_SUTRADARA, dataFilm.getSutradara());
        cv.put(KEY_PEMERAN, dataFilm.getPemeran());
        cv.put(KEY_SINOPSIS, dataFilm.getSinopsis());

        db.insert(TABLE_FILM, null, cv);
    }

    public void editFilm(Film dataFilm){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_JUDUL, dataFilm.getJudul());
        cv.put(KEY_TGL, sdFormat.format( dataFilm.getTanggal()));
        cv.put(KEY_GAMBAR, dataFilm.getGambar());
        cv.put(KEY_GENRE, dataFilm.getGenre());
        cv.put(KEY_SUTRADARA, dataFilm.getSutradara());
        cv.put(KEY_PEMERAN, dataFilm.getPemeran());
        cv.put(KEY_SINOPSIS, dataFilm.getSinopsis());

        db.update(TABLE_FILM, cv, KEY_ID_FILM + "=?", new String[]{String.valueOf(dataFilm.getIdFilm())});
        db.close();
    }

    public void hapusFilm(int idFilm){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_FILM, KEY_ID_FILM + "=?", new String[]{String.valueOf(idFilm)});
        db.close();
    }

    public ArrayList<Film> getAllFilm(){
        ArrayList<Film> dataFilm = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_FILM;
        SQLiteDatabase db = getReadableDatabase();
        Cursor csr = db.rawQuery(query, null);
        if(csr.moveToFirst()){
            do{
                Date tempDate = new Date();
                try {
                    tempDate = sdFormat.parse(csr.getString(2));
                } catch (ParseException er){
                    er.printStackTrace();
                }

                Film tempFilm = new Film(
                        csr.getInt(0),
                        csr.getString(1),
                        tempDate,
                        csr.getString(3),
                        csr.getString(4),
                        csr.getString(5),
                        csr.getString(6),
                        csr.getString(7)
                );

                dataFilm.add(tempFilm);
            } while (csr.moveToNext());
        }
        return dataFilm;
    }

    private String storeImageFile(int id){
        String location;
        Bitmap image = BitmapFactory.decodeResource(context.getResources(), id);
        location = InputActivity.saveImageToInternalStorage(image, context);
        return location;
    }

    private void inisialisasiFilmAwal(SQLiteDatabase db){
        int idFilm = 0;
        Date tempDate = new Date();
        Date tempDate1 = new Date();

        try {
            tempDate = sdFormat.parse("14/03/2017");
        } catch (ParseException er){
            er.printStackTrace();
        }

        Film film1 = new Film(
                idFilm,
                "Promise",
                tempDate,
                storeImageFile(R.drawable.promises),
                "Drama/Romansa",
                "Asep Kusdinar",
                "Dimas Anggara, Amanda Rawles, Boy Wiliam, Mikha Tambayong",
                "Film Promise menceritakan seorang cowok Jogja berwajah tampan namun lugu dan sederhana Rahman (Dimas Anggara), sangat berbeda dibanding sahabatnya sejak kecil, Aji. Aji (Boy Wiliam) dikenal sebagai playboy yang suka gonta-ganti pacar. Aji selalu punya keinginan, menjadikan Rahman bisa seperti dirinya. Bisa merasakan cinta dan memiliki wawasan lebih luas seperti dirinya. Namun cara-cara yang dilakukan Aji tidak sejalan dengan keinginan Rahman.Namun kehendak Aji mengubah Rahman dengan cara yang salah, justru menjadi awal semua perubahan Rahman ke arah yang tidak pernah Aji perkirakan sebelumnya. Rahman berubah drastis Perubahan drastis sikap Rahman, justru membuat ayahnya marah besar. Semenjak itulah, Rahman tidak pernah bertemu Aji lagi. Waktu dan tempat seakan memutus persahabatan keduanya.MILAN, 12 bulan kemudian. Milan kini menjadi kota dimana Rahman kuliah dan bekerja paruh waktu di sebuah toko kelontong. Kanya (Amanda Rawles) adalah gadis Jawa blasteran yang sejak usia 10 tahun tinggal di Eropa. Ia harus pulang ke Jogja untuk mendengarkan wasiat dari ibunya. Sang ibu berharap Kanya tidak kembali lagi ke Eropa untuk menjalankan wasiat yang ia berikan.Moza (Mikha Tambayong), teman kuliah Rahman, memiliki love interest kepada Rahman. Namun Moza melihat ada teka-teki dalam hidup Rahman yang Moza tak pernah bisa temukan jawabnya. Itu sebabnya, Moza ragu mengungkapkan rasa cintanya pada Rahman. Ia ingin mencari jawab teka-teki hidup Rahman sebelum mengungkapkan cintanya. Selain Moza, Salsabila (Mawar De Jongh), salah satu murid di pesantren ayahnya Rahman, juga punya janji cinta kepada Rahman. Ia ungkapkan perasaannya lewat surat yang ia titipkan melalui Aji. Suatu ketika, Rahman menerima telepon dari Aji. Rahman merasakan perubahan sikap sahabatnya. Bahkan pertemuan Aji dan Rahman sudah tidak seperti dulu lagi. Moza mulai memahami jati diri Rahman sesungguhnya. Termasuk perempuan yang dicintai Rahman. Siapa kira-kira perempuan tersebut? Apakah Moza atau Kanya, atau bahkan tidak keduanya?"

        );

        tambahFilm(film1, db);
        idFilm++;

        try {
            tempDate = sdFormat.parse("20/06/2013");
        } catch (ParseException er){
            er.printStackTrace();
        }

        Film film2 = new Film(
                idFilm,
                "Jokowi",
                tempDate,
                storeImageFile(R.drawable.jokowi),
                "Drama/Kisah Nyata",
                "Azhar Kinoi Lubis",
                "Teuku Rifnu Wikana, Prisia Nasution",
                "ni adalah cerita seorang anak tukang kayu bernama Joko Widodo, yang tinggal dan hidup di rumah kecil pinggiran sungai. Masa kanak-kanak yang jauh dari istilah berkecukupan telah dilaluinya. Namun hal itu tidak menyurutkan semangat anak kampung pemburu telor bebek ini untuk meneruskan sekolahnya ke pendidikan yang lebih tinggi. Kecintaannya pada Musik Rock yang tetap bertahan hingga saat ia menjadi pemimpin besar nantinya itu, seolah mampu memotivasi semangat hidupnya.\n" +
                        "\n" +
                        "Kisah cinta dengan Iriana, seorang gadis sederhana, teman sekolah adiknya menjadi pendorong semangat sang pemimpin masa depan ini untuk menghadapi berbagai tantangan. Sepeninggal Pak Notomiharjo, orang tua, guru sekaligus sahabatnya, Joko seperti tak mau tenggelam dalam kedukaan. Usahanya untuk membuktikan semua pelajaran dari sang ayah, makin keras ia lakukan. Dan waktu mengantarkan anak bantaran kali ini, menjadi sosok yang bukan hanya besar dimata orang-orang disekitarnya namun juga rendah hati dan selalu memanusiakan sesamanya. Dari pinggiran sungai di desa kecil bernama Srambatan, Joko telah mampu tampil menjadi pemimpin kota yang menulis lembar sejarah baru."
        );

        tambahFilm(film2, db);
        idFilm++;


    }

}
