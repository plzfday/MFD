package MFD;

import java.io.*;
import java.util.ArrayList;

class MusicList {
    static ArrayList<String> paths = new ArrayList<>();
    static ArrayList<String> name = new ArrayList<>();

    /**
     * SaveDataInFile
     * Save data into file so that when open app again
     * playlist is loaded automatically.
     *
     * @throws FileNotFoundException filenotfoundexception
     */
    static void SaveDataInFile() throws FileNotFoundException {
        File file = new File("./music_list.dat");
        PrintWriter pw = new PrintWriter(file);
        for (String path : paths) {
            pw.println(path);
        }
        if (paths.size() > 0) pw.println("Name");
        for (String aName : name) {
            pw.println(aName);
        }
        pw.close();
    }

    /**
     * LoadDataFromFile
     * Load data from file when program is opened
     *
     * @throws IOException ioexception
     */
    static void LoadDataFromFile(GUI gui) throws IOException {
        File file = new File("./music_list.dat");

        if (file.exists()) {
            boolean trigger = false;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null) {
                if (line.equals("Name")) trigger = true;
                else {
                    if (!trigger) MusicList.paths.add(line);
                    else {
                        MusicList.name.add(line);
	                    gui.uAddData_list(line);
                    }
                }
                line = reader.readLine();
            }
            gui.uDefaultToList();
        } else {
            System.out.println("There isn't such file");
        }
    }
}
