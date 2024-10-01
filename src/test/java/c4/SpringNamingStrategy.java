package c4;

import com.structurizr.component.Type;
import com.structurizr.component.naming.DefaultNamingStrategy;
import com.structurizr.component.naming.FullyQualifiedNamingStrategy;
import com.structurizr.component.naming.NamingStrategy;

public class SpringNamingStrategy extends FullyQualifiedNamingStrategy {
    @Override
    public String nameOf(Type type) {

        if (type.getName().endsWith("Impl")) {
            return super.nameOf(type).substring(0, super.nameOf(type).length() - 4);
        } else {
            return super.nameOf(type);
        }
    }
}
