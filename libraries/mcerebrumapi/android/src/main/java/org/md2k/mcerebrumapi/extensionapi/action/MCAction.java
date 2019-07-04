package org.md2k.mcerebrumapi.extensionapi.action;

import org.md2k.mcerebrumapi.extensionapi.ExtensionCallback;
import org.md2k.mcerebrumapi.extensionapi.Param;

/*
 * Copyright (c) 2016, The University of Memphis, MD2K Center

 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
public class MCAction {

    public interface Action {
        void run(Object obj, ActionCallback actionCallback);
    }


    public static final String TYPE = "ACTION";
    private String id;
    private String title;
    private String description;
    private Action action;

    public Param getParam() {
        return new Param(TYPE, id, title, description);
    }
    private MCAction(){}

    public static IMCAction.IITitle builder(){
        return new Builder();
    }
    public static class Builder implements IMCAction.IITitle, IMCAction.IIDescription, IMCAction.IIAction, IMCAction.IIBuilder, IMCAction.IIBuild{
        MCAction mcAction;
        Builder(){
            mcAction = new MCAction();
        }
        public Builder setId(String id){
            mcAction.id= id;
            return this;
        }
        public Builder setTitle(String title){
            mcAction.title = title;
            return this;
        }
        public Builder setDescription(String description){
            mcAction.description = description;
            return this;
        }
        public Builder setAction(Action action){
            mcAction.action = action;
            return this;
        }
        public MCAction build(){
            return mcAction;
        }

    }
}
