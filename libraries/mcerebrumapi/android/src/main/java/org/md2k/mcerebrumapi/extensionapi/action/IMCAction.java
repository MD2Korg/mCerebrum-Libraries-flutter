package org.md2k.mcerebrumapi.extensionapi.action;

public interface IMCAction {
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
        IIBuild setAction(MCAction.Action action);
    }
    interface IIBuild{
        MCAction build();
    }

}
