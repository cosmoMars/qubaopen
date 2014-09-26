/*
 * MATLAB Compiler: 5.1 (R2014a)
 * Date: Thu Sep 25 13:55:08 2014
 * Arguments: "-B" "macro_default" "-W" "java:createFit,MatFactory" "-T" "link:lib" "-d" 
 * "/Users/mars/Documents/MATLAB/createFit/for_testing" "-v" 
 * "/Users/mars/Documents/MATLAB/createFit.m" 
 * "class{MatFactory:/Users/mars/Documents/MATLAB/createFit.m}" 
 */

package com.qubaopen.survey.controller.point;

import com.mathworks.toolbox.javabuilder.MWComponentOptions;
import com.mathworks.toolbox.javabuilder.MWCtfClassLoaderSource;
import com.mathworks.toolbox.javabuilder.MWCtfExtractLocation;
import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.internal.MWMCR;

/**
 * <i>INTERNAL USE ONLY</i>
 */
public class CreateFitMCRFactory
{
   
    
    /** Component's uuid */
    private static final String sComponentId = "createFit_E7EB2D15CDCAE3177725B230A6DB75DD";
    
    /** Component name */
    private static final String sComponentName = "createFit";
    
   
    /** Pointer to default component options */
	private static final MWComponentOptions sDefaultComponentOptions = 
        new MWComponentOptions(
            MWCtfExtractLocation.EXTRACT_TO_CACHE, 
            new MWCtfClassLoaderSource(CreateFitMCRFactory.class)
        );
    
    
    private CreateFitMCRFactory()
    {
        // Never called.
    }
    
    public static MWMCR newInstance(MWComponentOptions componentOptions) throws MWException
    {
        if (null == componentOptions.getCtfSource()) {
            componentOptions = new MWComponentOptions(componentOptions);
            componentOptions.setCtfSource(sDefaultComponentOptions.getCtfSource());
        }
        return MWMCR.newInstance(
            componentOptions, 
            CreateFitMCRFactory.class, 
            sComponentName, 
            sComponentId,
            new int[]{8,3,0}
        );
    }
    
    public static MWMCR newInstance() throws MWException
    {
        return newInstance(sDefaultComponentOptions);
    }
}
