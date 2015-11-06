package com.pmoradi.compiler;

import com.google.template.soy.SoyFileSet;
import com.google.template.soy.SoyToJsSrcCompiler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class SoyCompiler {

    private File srcDir;
    private File destDir;

    public SoyCompiler(File srcDir, File destDir) {
        this.srcDir = srcDir;
        this.destDir = destDir;
    }

    public void compile() throws IOException {
        destDir.mkdirs();

        List<File> soys = new LinkedList<>();
        findSoys(srcDir, soys);

//        SoyFileSetBuilder soyFileSet = SoyFileSet.builder();
        
        List<SoyFileSet> soyFileSets = soys.stream().map(soyfile -> SoyFileSet.builder().add(soyfile).build()).collect(Collectors.toList());
//        SoyToJsSrcCompiler.main(getArgs(soys));
    }

    private String[] getArgs(List<String> files){
        List<String> args = new ArrayList<>();
        args.add("--srcs");
        args.addAll(files);

        args.add("--outputDirectory");
        args.add(destDir.getPath());

        args.add("--javaPackage");
        args.add(null);

        args.add("--javaClassNameSource");
        args.add(null);

        return args.toArray(new String[args.size()]);
    }

    private void findSoys(File src, List<File> list){
        if(src.isDirectory()){
            for(File file : src.listFiles()){
                if(file.isDirectory())
                    findSoys(file, list);
                else
                    list.add(file);
            }
        }
    }
}
