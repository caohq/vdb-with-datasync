package datasync.utils;

import org.apache.commons.compress.archivers.zip.UnixStat;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @program: DataSync
 * @description: compress files
 * @author: huangwei
 * @create: 2018-10-23 16:03
 **/
public class ZipUtils {

    public static void zipDirectory(File file, String path, ZipArchiveOutputStream zipArchiveOutputStream) throws IOException {
        if (path == null)
            path = new String();
        else if (!path.isEmpty())
            path += File.separatorChar;
        ZipArchiveEntry entry = new ZipArchiveEntry(file, path + file.getName());
        zipArchiveOutputStream.putArchiveEntry(entry);
        if (!file.isDirectory()) {
            entry.setUnixMode(UnixStat.DEFAULT_FILE_PERM);
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                IOUtils.copy(fileInputStream, zipArchiveOutputStream);
            } finally {
                zipArchiveOutputStream.closeArchiveEntry();
            }
        } else {
            entry.setUnixMode(UnixStat.DEFAULT_DIR_PERM);
            zipArchiveOutputStream.closeArchiveEntry();
            File[] files = file.listFiles();
            for (File child : files) {
                zipDirectory(child, path + file.getName(), zipArchiveOutputStream);
            }
        }
    }
}
