package org.warheim.di.metainstruction;

/**
 *
 * @author andy
 */
//TODO: provide more MI's
public interface MetaInstruction {
    
    public void register() throws MetaInstructionException;
    
    public String execute() throws MetaInstructionException;
    
}
