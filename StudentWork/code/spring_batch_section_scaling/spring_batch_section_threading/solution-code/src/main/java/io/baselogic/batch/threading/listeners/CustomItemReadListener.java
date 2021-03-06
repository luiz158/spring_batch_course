package io.baselogic.batch.threading.listeners;

import org.springframework.batch.core.annotation.AfterRead;
import org.springframework.batch.core.annotation.BeforeRead;
import org.springframework.batch.core.annotation.OnReadError;

@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
public class CustomItemReadListener {

    private org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    //---------------------------------------------------------------------------//
    // Lab: Create @BeforeRead and log step details
    @BeforeRead
    public void beforeRead(){
        log.info("______+BEFORE READ, executed on thread [{}]",
                Thread.currentThread().getName());
    }


    //---------------------------------------------------------------------------//
    // Lab: Create @BeforeRead and log step details
    @AfterRead
    public void afterRead(String item){
        log.info("______+AFTER READ: [{}]", item);
    }


    //---------------------------------------------------------------------------//
    // Lab: Create @BeforeRead and log step details
    @OnReadError
    public void onReadError(Exception e){
        log.error("______+AFTER ERROR: [{}]", e.getMessage());
    }

} // The End...
