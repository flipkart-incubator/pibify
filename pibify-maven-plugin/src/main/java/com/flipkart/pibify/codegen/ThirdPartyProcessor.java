package com.flipkart.pibify.codegen;

import com.flipkart.pibify.ThirdPartyProcessorResult;

/**
 * This interfaces exposes ways to plug third party library support.
 * Author bageshwar.pn
 * Date 31/10/24
 */
public interface ThirdPartyProcessor {

    ThirdPartyProcessorResult process(CodeGenSpec codeGenSpec, Class<?> type);

    /**
     * Should return a unique Id for the processor type
     *
     * @return
     */
    String getId();

}
