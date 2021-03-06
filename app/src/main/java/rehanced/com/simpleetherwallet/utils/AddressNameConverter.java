package rehanced.com.simpleetherwallet.utils;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import rehanced.com.simpleetherwallet.data.WalletDisplay;

public class AddressNameConverter {

    private HashMap<String, String> addressbook;
    private HashMap<String, String> wellknown_addresses;
    private static AddressNameConverter instance;

    public static AddressNameConverter getInstance(Context context) {
        if (instance == null)
            instance = new AddressNameConverter(context);
        return instance;
    }

    private AddressNameConverter(Context context) {
        try {
            load(context);
            if (!contains("0x65f24a05e4d56ff665c1766f145ec743ddf4e2f8")) {
                put("0x65f24a05e4d56ff665c1766f145ec743ddf4e2f8", "GithubCoin Development ✓", context);
            }
        } catch (Exception e) {
            addressbook = new HashMap<String, String>();
            put("0x65f24a05e4d56ff665c1766f145ec743ddf4e2f8", "GithubCoin Development ✓", context);
        }
        wellknown_addresses = new WellKnownAddresses();
    }

    public synchronized void put(String addresse, String name, Context context) {
        if (name == null || name.length() == 0)
            addressbook.remove(addresse);
        else
            addressbook.put(addresse, name.length() > 22 ? name.substring(0, 22) : name);
        save(context);
    }

    public String get(String addresse) {
        return addressbook.get(addresse);
    }

    public boolean contains(String addresse) {
        return addressbook.containsKey(addresse);
    }

    public ArrayList<WalletDisplay> getAsAddressbook() {
        ArrayList<WalletDisplay> erg = new ArrayList<WalletDisplay>();
        for (Map.Entry<String, String> entry : addressbook.entrySet()) {
            erg.add(new WalletDisplay(entry.getValue().toString(), entry.getKey().toString()));
        }
        Collections.sort(erg);
        return erg;
    }

    public synchronized void save(Context context) {
        FileOutputStream fout;
        try {
            fout = new FileOutputStream(new File(context.getFilesDir(), "namedb.dat"));
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(addressbook);
            oos.close();
            fout.close();
        } catch (Exception e) {
        }
    }

    @SuppressWarnings("unchecked")
    public synchronized void load(Context context) throws IOException, ClassNotFoundException {
        FileInputStream fout = new FileInputStream(new File(context.getFilesDir(), "namedb.dat"));
        ObjectInputStream oos = new ObjectInputStream(new BufferedInputStream(fout));
        addressbook = (HashMap<String, String>) oos.readObject();
        oos.close();
        fout.close();
    }

}
