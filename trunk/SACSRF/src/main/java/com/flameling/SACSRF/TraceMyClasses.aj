/*
Copyright (c) Xerox Corporation 1998-2002.  All rights reserved.

Use and copying of this software and preparation of derivative works based
upon this software are permitted.  Any distribution of this software or
derivative works must comply with all applicable United States export control
laws.

This software is made available AS IS, and Xerox Corporation makes no warranty
about the software, its performance or its conformity to any specification.
*/

package com.flameling.SACSRF;

import org.apache.maven.archiva.web.action.admin.appearance.EditOrganisationInfoAction;


/**
 *
 * This class connects the tracing functions in the Trace class with
 * the constructors and methods in the application classes.
 *
 */

aspect TraceMyClasses {

    pointcut myClass(): within(EditOrganisationInfoAction);
    
    /**
     * The methods of those classes.
     */
    pointcut myMethod(): myClass() && execution(* *(..));

    /**
     * Prints trace messages before and after executing methods.
     */
    before (): myMethod() {
    	System.out.println("Before method.");
    }
    after(): myMethod() {
        System.out.println("After method.");
    }
    
}

