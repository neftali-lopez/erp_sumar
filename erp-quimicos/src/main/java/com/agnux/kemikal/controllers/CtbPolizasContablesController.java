/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agnux.kemikal.controllers;

import com.agnux.cfd.v2.Base64Coder;
import com.agnux.common.helpers.StringHelper;
import com.agnux.common.helpers.TimeHelper;
import com.agnux.common.obj.DataPost;
import com.agnux.common.obj.ResourceProject;
import com.agnux.common.obj.UserSessionData;
import com.agnux.kemikal.interfacedaos.CtbInterfaceDao;
import com.agnux.kemikal.interfacedaos.HomeInterfaceDao;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Noe Martinez
 * gpmarsan@gmail.com
 * 01/septiembre/2014
 * 
 */
@Controller
@SessionAttributes({"user"})
@RequestMapping("/ctbpolizacontable/")
public class CtbPolizasContablesController {
    ResourceProject resource = new ResourceProject();
    private static final Logger log  = Logger.getLogger(CtbPolizasContablesController.class.getName());
    
    @Autowired
    @Qualifier("daoCtb")
    private CtbInterfaceDao ctbDao;
    
    public CtbInterfaceDao getCtbDao() {
        return ctbDao;
    }
    
    @Autowired
    @Qualifier("daoHome")
    private HomeInterfaceDao HomeDao;
    
    public HomeInterfaceDao getHomeDao() {
        return HomeDao;
    }
    
    @RequestMapping(value="/startup.agnux")
    public ModelAndView startUp(HttpServletRequest request, HttpServletResponse response, 
            @ModelAttribute("user") UserSessionData user)
            throws ServletException, IOException {
        log.log(Level.INFO, "Ejecutando starUp de {0}", CtbPolizasContablesController.class.getName());
        LinkedHashMap<String,String> infoConstruccionTabla = new LinkedHashMap<String,String>();
        
        infoConstruccionTabla.put("id", "Acciones:90");
        infoConstruccionTabla.put("poliza", "P&oacute;liza:80");
        infoConstruccionTabla.put("tipo", "Tipo de P&oacute;liza:170");
        infoConstruccionTabla.put("concepto","Concepto:100");
        infoConstruccionTabla.put("fecha","Fecha:90");
        infoConstruccionTabla.put("moneda", "Moneda:70");
        infoConstruccionTabla.put("debe","Debe:90");
        infoConstruccionTabla.put("haber","Haber:90");
        infoConstruccionTabla.put("status","Estatus:100");
        
                    
        ModelAndView x = new ModelAndView("ctbpolizacontable/startup", "title", "P&oacute;lizas Contables");
        
        x = x.addObject("layoutheader", resource.getLayoutheader());
        x = x.addObject("layoutmenu", resource.getLayoutmenu());
        x = x.addObject("layoutfooter", resource.getLayoutfooter());
        x = x.addObject("grid", resource.generaGrid(infoConstruccionTabla));
        x = x.addObject("url", resource.getUrl(request));
        x = x.addObject("username", user.getUserName());
        x = x.addObject("empresa", user.getRazonSocialEmpresa());
        x = x.addObject("sucursal", user.getSucursal());
        
        String userId = String.valueOf(user.getUserId());
        
        String codificado = Base64Coder.encodeString(userId);
        
        //id de usuario codificado
        x = x.addObject("iu", codificado);
        
        return x;
    }
    
    
    
    
    @RequestMapping(value="/getAllPolizas.json", method = RequestMethod.POST)
    public @ResponseBody HashMap<String,ArrayList<HashMap<String, Object>>> getAllPolizasJson(
           @RequestParam(value="orderby", required=true) String orderby,
           @RequestParam(value="desc", required=true) String desc,
           @RequestParam(value="items_por_pag", required=true) int items_por_pag,
           @RequestParam(value="pag_start", required=true) int pag_start,
           @RequestParam(value="display_pag", required=true) String display_pag,
           @RequestParam(value="input_json", required=true) String input_json,
           @RequestParam(value="cadena_busqueda", required=true) String cadena_busqueda,
           @RequestParam(value="iu", required=true) String id_user_cod,
           Model modcel
        ) {
           
        HashMap<String,ArrayList<HashMap<String, Object>>> jsonretorno = new HashMap<String,ArrayList<HashMap<String, Object>>>();
        HashMap<String,String> has_busqueda = StringHelper.convert2hash(StringHelper.ascii2string(cadena_busqueda));
        
        //Aplicativo Catalogo de Polizas Contables(CTB)
        Integer app_selected = 179;
        
        //decodificar id de usuario
        Integer id_usuario = Integer.parseInt(Base64Coder.decodeString(id_user_cod));
        
        //Variables para el buscador
        String sucursal = StringHelper.isNullString(String.valueOf(has_busqueda.get("sucursal")));
        String tipo_pol = StringHelper.isNullString(String.valueOf(has_busqueda.get("tipo_pol")));
        String status = StringHelper.isNullString(String.valueOf(has_busqueda.get("status")));
        String concepto = StringHelper.isNullString(String.valueOf(has_busqueda.get("concepto")));
        String poliza = "%"+StringHelper.isNullString(String.valueOf(has_busqueda.get("poliza")))+"%";
        String fecha_inicial = ""+StringHelper.isNullString(String.valueOf(has_busqueda.get("fecha_inicial")))+"";
        String fecha_final = ""+StringHelper.isNullString(String.valueOf(has_busqueda.get("fecha_final")))+"";
        
        String data_string = app_selected+"___"+id_usuario+"___"+sucursal+"___"+tipo_pol+"___"+status+"___"+concepto+"___"+poliza+"___"+fecha_inicial+"___"+fecha_final;
        System.out.println("data_string: "+data_string);
        //Obtiene total de registros en base de datos, con los parametros de busqueda
        int total_items = this.getCtbDao().countAll(data_string);
        
        //Calcula el total de paginas
        int total_pags = resource.calculaTotalPag(total_items,items_por_pag);
        
        //Variables que necesita el datagrid, para no tener que hacer uno por cada aplicativo
        DataPost dataforpos = new DataPost(orderby, desc, items_por_pag, pag_start, display_pag, input_json, cadena_busqueda,total_items,total_pags, id_user_cod);
        
        int offset = resource.__get_inicio_offset(items_por_pag, pag_start);
        
        //Obtiene los registros para el grid, de acuerdo a los parametros de busqueda
        jsonretorno.put("Data", this.getCtbDao().getPolizasContables_PaginaGrid(data_string, offset, items_por_pag, orderby, desc));
        
        //Obtiene el hash para los datos que necesita el datagrid
        jsonretorno.put("DataForGrid", dataforpos.formaHashForPos(dataforpos));
        
        return jsonretorno;
    }
    
    
    
    @RequestMapping(method = RequestMethod.POST, value="/getCuentasMayor.json")
    public @ResponseBody HashMap<String,Object> getCuentasMayorJson(
            @RequestParam(value="iu", required=true) String id_user,
            Model model
        ) {
        
        log.log(Level.INFO, "Ejecutando getCuentasMayorJson de {0}", CtbPolizasContablesController.class.getName());
        HashMap<String,Object> jsonretorno = new HashMap<String,Object>();
        HashMap<String, String> userDat = new HashMap<String, String>();
        HashMap<String, Object> data = new HashMap<String, Object>();
        
        
        //Decodificar id de usuario
        Integer id_usuario = Integer.parseInt(Base64Coder.decodeString(id_user));
        userDat = this.getHomeDao().getUserById(id_usuario);
        Integer id_empresa = Integer.parseInt(userDat.get("empresa_id"));
        Integer id_sucursal = Integer.parseInt(userDat.get("sucursal_id"));
        
        data.put("suc", id_sucursal);
        data.put("nivel_cta", userDat.get("nivel_cta"));
        data.put("fecha_actual", TimeHelper.getFechaActualYMD());
        
        if(this.getCtbDao().getUserRolAdmin(id_usuario)>0){
            data.put("versuc", true);
        }else{
            data.put("versuc", false);
        }
        
        jsonretorno.put("CtaMay", this.getCtbDao().getPolizasContables_CuentasMayor(id_empresa));
        jsonretorno.put("Suc", this.getCtbDao().getCtb_Sucursales(id_empresa));
        jsonretorno.put("Data", data);
        jsonretorno.put("Anios", this.getCtbDao().getPolizasContables_Anios());
        jsonretorno.put("TPol", this.getCtbDao().getPolizasContables_TiposPolizas(id_empresa));
        jsonretorno.put("Con", this.getCtbDao().getPolizasContables_Conceptos(id_empresa));
        
        return jsonretorno;
    }
    
    
    @RequestMapping(method = RequestMethod.POST, value="/getPoliza.json")
    public @ResponseBody HashMap<String,ArrayList<HashMap<String, Object>>> getPolizaJson(
            @RequestParam(value="id", required=true) Integer id,
            @RequestParam(value="iu", required=true) String id_user,
            Model model
        ) {
        
        log.log(Level.INFO, "Ejecutando getPolizaJson de {0}", CtbPolizasContablesController.class.getName());
        HashMap<String,ArrayList<HashMap<String, Object>>> jsonretorno = new HashMap<String,ArrayList<HashMap<String, Object>>>();
        ArrayList<HashMap<String, Object>> datosPoliza = new ArrayList<HashMap<String, Object>>();
        HashMap<String, String> userDat = new HashMap<String, String>();
        ArrayList<HashMap<String, Object>> sucursales = new ArrayList<HashMap<String, Object>>();
        ArrayList<HashMap<String, Object>> arrayExtra = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> extra = new HashMap<String, Object>();
        
        //Decodificar id de usuario
        Integer id_usuario = Integer.parseInt(Base64Coder.decodeString(id_user));
        userDat = this.getHomeDao().getUserById(id_usuario);
        
        Integer id_empresa = Integer.parseInt(userDat.get("empresa_id"));
        
        //Esta variable indica si la empresa incluye modulo de Contabilidad
        extra.put("incluye_contab", userDat.get("incluye_contab"));
        extra.put("nivel_cta", userDat.get("nivel_cta"));
        extra.put("fecha_actual", TimeHelper.getFechaActualYMD());
        
        arrayExtra.add(0,extra);
        
        if( id != 0  ){
            datosPoliza = this.getCtbDao().getPolizasContables_Datos(id);
        }
        
        //jsonretorno.put("Suc", this.getCtbDao().getCtb_Sucursales(id_empresa));
        jsonretorno.put("Monedas", this.getCtbDao().getMonedas());
        //jsonretorno.put("TPol", this.getCtbDao().getPolizasContables_TiposPolizas(id_empresa));
        //jsonretorno.put("Con", this.getCtbDao().getPolizasContables_Conceptos(id_empresa));
        jsonretorno.put("CC", this.getCtbDao().getPolizasContables_CentrosCostos(id_empresa));
        //jsonretorno.put("CtaMay", this.getCtbDao().getPolizasContables_CuentasMayor(id_empresa));
        jsonretorno.put("Anios", this.getCtbDao().getPolizasContables_Anios());
        
        
        jsonretorno.put("Data", datosPoliza);
        jsonretorno.put("Extras", arrayExtra);
        
        return jsonretorno;
    }
    
    
    //metodo para el Buscador de Cuentas Contables
    @RequestMapping(method = RequestMethod.POST, value="/getBuscadorCuentasContables.json")
    public @ResponseBody HashMap<String,ArrayList<HashMap<String, String>>> getBuscadorCuentasContablesJson(
            @RequestParam(value="cta_mayor", required=true) Integer cta_mayor,
            @RequestParam(value="detalle", required=true) Integer detalle,
            @RequestParam(value="clasifica", required=false) String clasifica,
            @RequestParam(value="cta", required=false) String cta,
            @RequestParam(value="scta", required=false) String scta,
            @RequestParam(value="sscta", required=false) String sscta,
            @RequestParam(value="ssscta", required=false) String ssscta,
            @RequestParam(value="sssscta", required=false) String sssscta,
            @RequestParam(value="descripcion", required=false) String descripcion,
            @RequestParam(value="iu", required=true) String id_user,
            Model model
        ) {
        
        log.log(Level.INFO, "Ejecutando getBuscadorCuentasContablesJson de {0}", CtbPolizasContablesController.class.getName());
        HashMap<String,ArrayList<HashMap<String, String>>> jsonretorno = new HashMap<String,ArrayList<HashMap<String, String>>>();
        HashMap<String, String> userDat = new HashMap<String, String>();
        ArrayList<HashMap<String, String>> cuentasContables = new ArrayList<HashMap<String, String>>();
        
        //decodificar id de usuario
        Integer id_usuario = Integer.parseInt(Base64Coder.decodeString(id_user));
        userDat = this.getHomeDao().getUserById(id_usuario);
        Integer id_empresa = Integer.parseInt(userDat.get("empresa_id"));
        
        System.out.println("cta_mayor: "+cta_mayor);
        
        cuentasContables = this.getCtbDao().getPolizasContables_CuentasContables(cta_mayor, detalle, clasifica, cta, scta, sscta, ssscta, sssscta, descripcion, id_empresa);
        
        jsonretorno.put("CtaContables", cuentasContables);
        
        return jsonretorno;
    }
    
    
    
    
    //metodo para el Buscador de Cuentas Contables
    @RequestMapping(method = RequestMethod.POST, value="/getDataCta.json")
    public @ResponseBody HashMap<String,ArrayList<HashMap<String, Object>>> getDataCtaJson(
            @RequestParam(value="detalle", required=true) Integer detalle,
            @RequestParam(value="cta", required=false) String cta,
            @RequestParam(value="scta", required=false) String scta,
            @RequestParam(value="sscta", required=false) String sscta,
            @RequestParam(value="ssscta", required=false) String ssscta,
            @RequestParam(value="sssscta", required=false) String sssscta,
            @RequestParam(value="iu", required=true) String id_user,
            Model model
        ) {
        
        log.log(Level.INFO, "Ejecutando getDataCtaJson de {0}", CtbPolizasContablesController.class.getName());
        HashMap<String,ArrayList<HashMap<String, Object>>> jsonretorno = new HashMap<String,ArrayList<HashMap<String, Object>>>();
        HashMap<String, String> userDat = new HashMap<String, String>();
        
        //Decodificar id de usuario
        Integer id_usuario = Integer.parseInt(Base64Coder.decodeString(id_user));
        userDat = this.getHomeDao().getUserById(id_usuario);
        Integer id_empresa = Integer.parseInt(userDat.get("empresa_id"));
        Integer id_sucursal = Integer.parseInt(userDat.get("sucursal_id"));
        
        if(this.getCtbDao().getUserRolAdmin(id_usuario)>0){
            //Sucursal cero cuando el usuario es administrador, esto para permitir la busque de la cuenta contable sin importar la sucursal
            id_sucursal=0;
        }
        
        jsonretorno.put("Cta", this.getCtbDao().getDatosCuentaContable(detalle, cta, scta, sscta, ssscta, sssscta, id_empresa, id_sucursal));
        
        return jsonretorno;
    }

	
	
    
    //crear y editar
    @RequestMapping(method = RequestMethod.POST, value="/edit.json")
    public @ResponseBody HashMap<String, String> editJson(
            @RequestParam(value="identificador", required=true) String identificador,
            @RequestParam(value="select_sucursal", required=true) String select_sucursal,
            @RequestParam(value="select_mes", required=true) String select_mes,
            @RequestParam(value="select_anio", required=true) String select_anio,
            @RequestParam(value="select_tipo", required=true) String select_tipo,
            @RequestParam(value="select_moneda", required=true) String select_moneda,
            @RequestParam(value="select_concepto", required=true) String select_concepto,
            @RequestParam(value="select_centro_costo", required=true) String select_centro_costo,
            @RequestParam(value="fecha", required=true) String fecha,
            @RequestParam(value="descripcion_cuenta", required=true) String descripcion,
            @RequestParam(value="observacion", required=true) String observacion,
            @RequestParam(value="debe", required=true) String debe,
            @RequestParam(value="haber", required=true) String haber,
            @RequestParam(value="id_cta", required=true) String id_cta,
            @RequestParam(value="cuenta", required=true) String cuenta,
            @RequestParam(value="scuenta", required=true) String scuenta,
            @RequestParam(value="sscuenta", required=true) String sscuenta,
            @RequestParam(value="ssscuenta", required=true) String ssscuenta,
            @RequestParam(value="sssscuenta", required=true) String sssscuenta,
            Model model,@ModelAttribute("user") UserSessionData user
        ) {
        
        HashMap<String, String> jsonretorno = new HashMap<String, String>();
        HashMap<String, String> succes = new HashMap<String, String>();
        //Aplicativo Catalogo de Polizas Contables(CTB)
        Integer app_selected = 179;
        String command_selected = "new";
        Integer id_usuario= user.getUserId();//variable para el id  del usuario
        String extra_data_array = "'sin datos'";
        String actualizo = "0";
        
        if( identificador.equals("0") ){
            command_selected = "new";
        }else{
            command_selected = "edit";
        }
        
        //System.out.println("app_selected="+app_selected+"\ncommand_selected="+command_selected+"\nid_usuario="+id_usuario+"\nidentificador="+identificador+"\nselect_sucursal="+select_sucursal+"\nselect_mes="+select_mes+"\nselect_anio="+select_anio+"\nselect_tipo="+select_tipo+"\nselect_moneda="+select_moneda+"\nselect_concepto="+select_concepto+"\nselect_centro_costo="+select_centro_costo+"\nfecha="+fecha+"\ndescripcion="+descripcion+"\nobservacion="+observacion+"\ndebe="+debe+"\nhaber="+haber+"\nid_cta="+id_cta+"\ncuenta="+cuenta+"\nscuenta="+scuenta+"\nsscuenta="+sscuenta+"\nssscuenta="+ssscuenta+"\nsssscuenta="+sssscuenta);
        
        String data_string = 
                app_selected+"___"+
                command_selected+"___"+
                id_usuario+"___"+
                identificador+"___"+
                select_sucursal+"___"+
                select_mes+"___"+
                select_anio+"___"+
                select_tipo+"___"+
                select_moneda+"___"+
                select_concepto+"___"+
                select_centro_costo+"___"+
                fecha+"___"+
                descripcion+"___"+
                observacion+"___"+
                debe+"___"+
                haber+"___"+
                id_cta+"___"+
                cuenta+"___"+
                scuenta+"___"+
                sscuenta+"___"+
                ssscuenta+"___"+
                sssscuenta;
        
        succes = this.getCtbDao().selectFunctionValidateAaplicativo(data_string,app_selected,extra_data_array);
        
        log.log(Level.INFO, "despues de validacion {0}", String.valueOf(succes.get("success")));
        if( String.valueOf(succes.get("success")).equals("true") ){
            actualizo = this.getCtbDao().selectFunctionForCtbAdmProcesos(data_string, extra_data_array);
        }
        
        jsonretorno.put("success",String.valueOf(succes.get("success")));
        
        log.log(Level.INFO, "Salida json {0}", String.valueOf(jsonretorno.get("success")));
        return jsonretorno;
    }
    
    
    
    
    
}
