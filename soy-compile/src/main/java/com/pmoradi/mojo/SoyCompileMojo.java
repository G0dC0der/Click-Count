package com.pmoradi.mojo;

import com.pmoradi.soycompiler.SoyCompiler;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;

@Mojo(name = "soy:compile")
public class SoyCompileMojo extends AbstractMojo{

    @Parameter(property = "soy:compile.srcDir")
    private String srcDir;

    @Parameter(property = "soy:compile.destDir")
    private String destDir;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if(srcDir == null)
            throw new MojoExecutionException("The src directory must be set");
        if(destDir == null)
            getLog().warn("No destination folder is set. Will use the target folder.");

        SoyCompiler compiler = new SoyCompiler(new File(srcDir), new File(destDir));
        try {
            compiler.compile();
        } catch (IOException e) {
            throw new MojoExecutionException("Soy Compile Error", e);
        }
    }
}
