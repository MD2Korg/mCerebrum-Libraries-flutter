package org.md2k.mcerebrumapi.extensionapi.user_interface;


public interface IMCUserInterface {
    interface IITitle{
        IIDescription setId(String id);
    }
    interface IIDescription{
        IIAction setTitle(String title);
    }
    interface IIAction{
        IIBuilder setDescription(String description);
    }
    interface IIBuilder{
        IIBuild setAction(MCUserInterface.UserInterface userInterface);
    }
    interface IIBuild{
        MCUserInterface build();
    }

}
