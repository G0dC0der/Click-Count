package com.pmoradi.mojo;

import com.pmoradi.compiler.SoyCompiler;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;

@Mojo(name = "soytransform", defaultPhase = LifecyclePhase.COMPILE)
public class SoyCompileMojo extends AbstractMojo {

    @Parameter(property = "srcDir", required = true)
    private String srcDir;

    @Parameter(property = "destDir", required = true)
    private String destDir;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        SoyCompiler compiler = new SoyCompiler(new File(srcDir), new File(destDir));
        try {
            compiler.compile();
        } catch (IOException e) {
            throw new MojoExecutionException("Soy compilation error.", e);
        }
    }
}