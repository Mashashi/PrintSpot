package de.remotes;
import javax.ejb.Remote;

import de.validation.ValidationException;

@Remote
public interface CustomerMngmtRemote {
 
    Integer register(de.database.insert.Customer c) throws ValidationException;
    
    de.database.get.Customer get(Integer id);
    
    void discard(Integer id);
    
    void approve(Integer idEmployee, Integer id);
    
}