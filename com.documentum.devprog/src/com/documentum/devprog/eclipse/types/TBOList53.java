/*******************************************************************************
 * Copyright (c) 2005-2006, EMC Corporation 
 * All rights reserved.

 * Redistribution and use in source and binary forms, 
 * with or without modification, are permitted provided that 
 * the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright 
 *   notice, this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * - Neither the name of the EMC Corporation nor the names of its 
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR 
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT 
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT 
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY 
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE 
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 *******************************************************************************/

/*
 * Created on Sep 22, 2005
 *
 * EMC Documentum Developer Program 2005
 */
package com.documentum.devprog.eclipse.types;

import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfLogger;

import com.documentum.fc.client.IDfEnumeration;
import com.documentum.fc.client.IDfLocalModuleRegistry;
import com.documentum.fc.client.IDfModuleDescriptor;
import com.documentum.fc.client.IDfSession;

import com.documentum.devprog.eclipse.common.PluginState;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class TBOList53
{
    
    private static HashMap tbos = new HashMap();

    public static boolean hasTBO(String repoName,String name)
    {
        if(tbos.containsKey(repoName) == false)
        {
            refreshTBOList(repoName);
        }
        Set s = (Set) tbos.get(repoName);
        return s.contains(name);       
    }

    
    public static void refreshTBOList(String repoName)
    {        
        HashSet s = new HashSet();
        IDfSession sess = null;
        try
        {
            sess = PluginState.getSession(repoName);
            IDfLocalModuleRegistry reg = sess.getModuleRegistry();
            IDfEnumeration enums = reg.getTboDescriptors();
            while(enums.hasMoreElements())
            {
                IDfModuleDescriptor desc = (IDfModuleDescriptor)enums.nextElement();
                s.add(desc.getObjectName());                    
            }
        }
        catch(DfException dfe)
        {
            DfLogger.error(TBOList53.class.getName(),"Error getting tbo names",null,dfe);
        }        
        finally
        {
            PluginState.releaseSession(sess);
        }
        tbos.put(repoName,s);
        
    }
}
