package ${packetagePojo};

import java.lang.*;

public class ${entityName} {

<#list dataStrutes as dataStrute>

    private ${dataStrute.type} ${dataStrute.column};
</#list>
<#list dataStrutes as dataStrute>

    public ${dataStrute.type} get${dataStrute.pColumn}() {
    return ${dataStrute.column};
    }

    public void set${dataStrute.pColumn}(String ${dataStrute.column}) {
    this.${dataStrute.column} = ${dataStrute.column};
    }
</#list>

}