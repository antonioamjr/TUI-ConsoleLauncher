package ohi.andre.consolelauncher.commands.main.raw;

import ohi.andre.consolelauncher.R;
import ohi.andre.consolelauncher.commands.CommandAbstraction;
import ohi.andre.consolelauncher.commands.ExecutePack;
import ohi.andre.consolelauncher.commands.specific.PermanentSuggestionCommand;
import ohi.andre.consolelauncher.commands.main.MainPack;
import ohi.andre.consolelauncher.tuils.Tuils;

/**
 * Created by francescoandreuzzi on 29/01/2017.
 */

public class calc extends PermanentSuggestionCommand {

    @Override
    public String exec(ExecutePack pack) throws Exception {
        try {
            return String.valueOf(Tuils.eval(pack.get(String.class, 0)));
        } catch (Exception e) {
            return ((MainPack) pack).res.getString(R.string.output_invalidcalc) + Tuils.NEWLINE + e.toString();
        }
    }

    @Override
    public int minArgs() {
        return 1;
    }

    @Override
    public int maxArgs() {
        return 1;
    }

    @Override
    public int[] argType() {
        return new int[] {CommandAbstraction.PLAIN_TEXT};
    }

    @Override
    public int priority() {
        return 3;
    }

    @Override
    public int helpRes() {
        return R.string.help_calc;
    }

    @Override
    public String onArgNotFound(ExecutePack pack) {
        return null;
    }

    @Override
    public String onNotArgEnough(ExecutePack pack, int nArgs) {
        MainPack info = (MainPack) pack;
        return info.res.getString(helpRes());
    }

    @Override
    public String[] parameters() {
        return null;
    }

    @Override
    public String[] permanentSuggestions() {
        return new String[] {"(", ")", "+", "-", "*", "/", "%", "^", "sqrt"};
    }
}
