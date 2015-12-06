package org.warheim.di;

/**
 *
 * @author andy
 */
public interface MetaInstruction {
    
    public void register() throws MetaInstructionException;
    
    public String execute() throws MetaInstructionException;
    
}
