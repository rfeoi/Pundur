package de.rfeoi.pundur;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class Config
{
    public static class Common
    {
        public final ForgeConfigSpec.ConfigValue<Integer> port;


        public Common(ForgeConfigSpec.Builder builder)
        {
            builder.push("webserver");
            this.port = builder.comment("This defines the port the internal web server will run on. This is only relevant on server side. Default is 8000")
                    .worldRestart()
                    .defineInRange("Short but readable name", 8000, 1, 65535);
            builder.pop();
        }
    }

    public static final Common SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;

    static //constructor
    {
        Pair<Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Common::new);
        SERVER = commonSpecPair.getLeft();
        SERVER_SPEC = commonSpecPair.getRight();
    }
}