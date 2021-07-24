package Dictionary;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import com.example.spinnertrial.dataBase;
import com.example.spinnertrial.*;

public class dictionary {

    public Dictionary function_values = new Hashtable();
    public String[][] keys = new String[][]{{"b8","b10","b12","b16","b20"},{"k8","k10","k12","k16","k20"},{"r8","r10","r12","r16","r20"}};
    Context context;




    public static List<Integer> bundle;
    public static List<Integer> kg;
    public static List<Integer> rod;



    public dictionary(List<Integer> bundle, List<Integer> kg, List<Integer> rod) {
        this.bundle = bundle;
        this.kg = kg;
        this.rod = rod;
    }


    public dictionary()
    {
    }
    public dictionary(Context context){
        this.context = context;
        dataBase db = new dataBase(context, "Default_Preset");
        bundle = db.getValues("BUNDLES");
        kg = db.getValues("KGs");
        rod = db.getValues("RODS");
    }





    public void calculate(){
        calc_interface b8k=(x)->(int)Math.ceil(x*kg.get(0));
        calc_interface b8r=(x)->(int)Math.ceil(x*rod.get(0));
        function_values.put("b8",new calc_interface[]{b8k,b8r});


        calc_interface b10k=(x)->(int)Math.ceil(x*kg.get(1));
        calc_interface b10r=(x)->(int)Math.ceil(x*rod.get(1));
        function_values.put("b10",new calc_interface[]{b10k,b10r});

        calc_interface b12k=(x)->(int)Math.ceil(x*kg.get(2));
        calc_interface b12r=(x)->(int)Math.ceil(x*rod.get(2));
        function_values.put("b12",new calc_interface[]{b12k,b12r});

        calc_interface b16k=(x)->(int)Math.ceil(x*kg.get(3));
        calc_interface b16r=(x)->(int)Math.ceil(x*rod.get(3));
        function_values.put("b16",new calc_interface[]{b16k,b16r});

        calc_interface b20k=(x)->(int)Math.ceil(x*kg.get(4));
        calc_interface b20r=(x)->(int)Math.ceil(x*rod.get(4));
        function_values.put("b20",new calc_interface[]{b20k,b20r});




        calc_interface k8b=(x)->(int)Math.ceil(x/kg.get(0));
        calc_interface k8r=(x)->(int)Math.ceil((x*rod.get(0))/kg.get(0));
        function_values.put("k8",new calc_interface[]{k8b,k8r});


        calc_interface k10b=(x)->(int)Math.ceil(x/kg.get(1));
        calc_interface k10r=(x)->(int)Math.ceil((x*rod.get(1))/kg.get(1));
        function_values.put("k10",new calc_interface[]{k10b,k10r});

        calc_interface k12b=(x)->(int)Math.ceil(x/kg.get(2));
        calc_interface k12r=(x)->(int)Math.ceil((x*rod.get(2))/kg.get(2));
        function_values.put("k12",new calc_interface[]{k12b,k12r});

        calc_interface k16b=(x)->(int)Math.ceil(x/kg.get(3));
        calc_interface k16r=(x)->(int)Math.ceil((x*rod.get(3))/kg.get(3));
        function_values.put("k16",new calc_interface[]{k16b,k16r});

        calc_interface k20b=(x)->(int)Math.ceil(x/kg.get(4));
        calc_interface k20r=(x)->(int)Math.ceil((x*rod.get(4))/kg.get(4));
        function_values.put("k20",new calc_interface[]{k20b,k20r});




        calc_interface r8b=(x)->(int)Math.ceil(x/rod.get(0));
        calc_interface r8k=(x)->(int)Math.ceil((x*kg.get(0))/rod.get(0));
        function_values.put("r8",new calc_interface[]{r8b,r8k});

        calc_interface r10b=(x)->(int)Math.ceil(x/rod.get(1));
        calc_interface r10k=(x)->(int)Math.ceil((x*kg.get(1))/rod.get(1));
        function_values.put("r10",new calc_interface[]{r10b,r10k});

        calc_interface r12b=(x)->(int)Math.ceil(x/rod.get(2));
        calc_interface r12k=(x)->(int)Math.ceil((x*kg.get(2))/rod.get(2));;
        function_values.put("r12",new calc_interface[]{r12b,r12k});

        calc_interface r16b=(x)->(int)Math.ceil(x/rod.get(3));
        calc_interface r16k=(x)->(int)Math.ceil((x*kg.get(3))/rod.get(3));;
        function_values.put("r16",new calc_interface[]{r16b,r16k});

        calc_interface r20b=(x)->(int)Math.ceil(x/rod.get(4));
        calc_interface r20k=(x)->(int)Math.ceil((x*kg.get(4))/rod.get(4));;
        function_values.put("r20",new calc_interface[]{r20b,r20k});
    }


}