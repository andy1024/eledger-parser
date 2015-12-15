package org.warheim.di.metainstruction;

/**
 *
 * @author andy
 */
public interface MetaInstruction {
    
    public void register() throws MetaInstructionException;
    
    public String execute() throws MetaInstructionException;
    
}
