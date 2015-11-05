package com.pmoradi.soycompiler;

import com.google.template.soy.SoyToJsSrcCompiler;

import java.io.File;
import java.io.IOException;

public class SoyCompiler {

    private File srcDir;
    private File destDir;

    public SoyCompiler(File srcDir, File destDir) {
        this.srcDir = srcDir;
        this.destDir = destDir;
    }

    public void compile() throws IOException {
        //Dont forget to compile the directory tree of srcDir.
        destDir.mkdirs();

        SoyToJsSrcCompiler.main(new String[]{String.format("--srcs %s --outputDirectory %s --javaPackage %s --javaClassNameSource %s",
                null,
                destDir.getAbsolutePath(),
                null,
                null)});
    }
}
