package com.misc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import misc.MQOConverter;
import store.codec.model.Mesh;

import java.io.File;
import java.io.IOException;

/**
 * @author ReverendDread on 7/15/2020
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
@RequiredArgsConstructor @Getter
public class MeshHolder {

    private final File file;

    public Mesh getMesh() {
        Mesh mesh = new Mesh();
        try {
            switch (getFormat()) {
                case DAT:
                    byte[] data = java.nio.file.Files.readAllBytes(file.toPath());
                    mesh.decode(data, false);
                    break;
                case MQO:
                    MQOConverter converter = new MQOConverter();
                    mesh = converter.assembleMesh(file);
                    break;
                case GZIP:
                    throw new IllegalStateException("Unhandled format!!!");
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return mesh;
    }

    public Format getFormat() {
        String extension = com.google.common.io.Files.getFileExtension(file.getName());
        switch (extension) {
            case "dat":
                return Format.DAT;
            case "gzip":
                return Format.GZIP;
            case "mqo":
                return Format.MQO;
            default:
                return Format.DAT;
        }
    }

    public enum Format {
        MQO, DAT, GZIP
    }

    @Override
    public String toString() {
        return file.getName();
    }

}
