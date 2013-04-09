$(function() {
	String.prototype.toCharCode = function(){
		var str = this.split(''), len = str.length, work = new Array(len);
		for (var i = 0; i < len; ++i){
			work[i] = this.charCodeAt(i);
		}
		return work.join(',');
	};

	$('#header').find('#header1').find('span.emp').text($('#lienzo_recalculable').find('input[name=emp]').val());
	$('#header').find('#header1').find('span.suc').text($('#lienzo_recalculable').find('input[name=suc]').val());
	var $username = $('#header').find('#header1').find('span.username');
	$username.text($('#lienzo_recalculable').find('input[name=user]').val());

	var $contextpath = $('#lienzo_recalculable').find('input[name=contextpath]');
	var controller = $contextpath.val()+"/controllers/cotpoliticasdepago";

    //Barra para las acciones
    $('#barra_acciones').append($('#lienzo_recalculable').find('.table_acciones'));
    $('#barra_acciones').find('.table_acciones').css({'display':'block'});
	var $new_cotpoliticasdepago = $('#barra_acciones').find('.table_acciones').find('a[href*=new_item]');
	var $visualiza_buscador = $('#barra_acciones').find('.table_acciones').find('a[href*=visualiza_buscador]');

	$('#barra_acciones').find('.table_acciones').find('#nItem').mouseover(function(){
		$(this).removeClass("onmouseOutNewItem").addClass("onmouseOverNewItem");
	});
	$('#barra_acciones').find('.table_acciones').find('#nItem').mouseout(function(){
		$(this).removeClass("onmouseOverNewItem").addClass("onmouseOutNewItem");
	});

	$('#barra_acciones').find('.table_acciones').find('#vbuscador').mouseover(function(){
		$(this).removeClass("onmouseOutVisualizaBuscador").addClass("onmouseOverVisualizaBuscador");
	});
	$('#barra_acciones').find('.table_acciones').find('#vbuscador').mouseout(function(){
		$(this).removeClass("onmouseOverVisualizaBuscador").addClass("onmouseOutVisualizaBuscador");
	});


	//aqui va el titulo del catalogo
	$('#barra_titulo').find('#td_titulo').append('Politicas de Pago');

	//barra para el buscador
	$('#barra_buscador').append($('#lienzo_recalculable').find('.tabla_buscador'));
	$('#barra_buscador').find('.tabla_buscador').css({'display':'block'});



	var $cadena_busqueda = "";
	var $busqueda_politica = $('#barra_buscador').find('.tabla_buscador').find('input[name=Busqueda_Politica]');

	var $buscar = $('#barra_buscador').find('.tabla_buscador').find('input[value$=Buscar]');
	var $limpiar = $('#barra_buscador').find('.tabla_buscador').find('input[value$=Limpiar]');



	var to_make_one_search_string = function(){
		var valor_retorno = "";
		var signo_separador = "=";
		valor_retorno += "titulo" + signo_separador + $busqueda_politica.val() + "|";
		valor_retorno += "iu" + signo_separador + $('#lienzo_recalculable').find('input[name=iu]').val() + "|";
		return valor_retorno;
	};

	cadena = to_make_one_search_string();

	$cadena_busqueda = cadena.toCharCode();
	//$cadena_busqueda = cadena;

	$buscar.click(function(event){
		event.preventDefault();
		cadena = to_make_one_search_string();
		$cadena_busqueda = cadena.toCharCode();
		$get_datos_grid();
	});



	$limpiar.click(function(event){
		event.preventDefault();
		$busqueda_politica.val('');
		//$busqueda_descripcion.val('');
	});



	TriggerClickVisializaBuscador = 0;
	//visualizar  la barra del buscador
	$visualiza_buscador.click(function(event){
		event.preventDefault();

		var alto=0;
		if(TriggerClickVisializaBuscador==0){
			 TriggerClickVisializaBuscador=1;
			 var height2 = $('#cuerpo').css('height');
			 //alert('height2: '+height2);

			 alto = parseInt(height2)-220;
			 var pix_alto=alto+'px';
			 //alert('pix_alto: '+pix_alto);

			 $('#barra_buscador').find('.tabla_buscador').css({'display':'block'});
			 $('#barra_buscador').animate({height: '60px'}, 500);
			 $('#cuerpo').css({'height': pix_alto});

			 //alert($('#cuerpo').css('height'));
		}else{
			 TriggerClickVisializaBuscador=0;
			 var height2 = $('#cuerpo').css('height');
			 alto = parseInt(height2)+220;
			 var pix_alto=alto+'px';

			 $('#barra_buscador').animate({height:'0px'}, 500);
			 $('#cuerpo').css({'height': pix_alto});
		};
	});




	$tabs_li_funxionalidad = function(){

		$('#forma-cotpoliticasdepago-window').find('#submit').mouseover(function(){
			$('#forma-cotpoliticasdepago-window').find('#submit').removeAttr("src").attr("src","../../img/modalbox/bt1.png");
			//$('#forma-centrocostos-window').find('#submit').css({backgroundImage:"url(../../img/modalbox/bt1.png)"});
		});
		$('#forma-cotpoliticasdepago-window').find('#submit').mouseout(function(){
			$('#forma-cotpoliticasdepago-window').find('#submit').removeAttr("src").attr("src","../../img/modalbox/btn1.png");
			//$('#forma-centrocostos-window').find('#submit').css({backgroundImage:"url(../../img/modalbox/btn1.png)"});
		});
		$('#forma-cotpoliticasdepago-window').find('#boton_cancelar').mouseover(function(){
			$('#forma-cotpoliticasdepago-window').find('#boton_cancelar').css({backgroundImage:"url(../../img/modalbox/bt2.png)"});
		})
		$('#forma-cotpoliticasdepago-window').find('#boton_cancelar').mouseout(function(){
			$('#forma-cotpoliticasdepago-window').find('#boton_cancelar').css({backgroundImage:"url(../../img/modalbox/btn2.png)"});
		});

		$('#forma-cotpoliticasdepago-window').find('#close').mouseover(function(){
			$('#forma-cotpoliticasdepago-window').find('#close').css({backgroundImage:"url(../../img/modalbox/close_over.png)"});
		});
		$('#forma-cotpoliticasdepago-window').find('#close').mouseout(function(){
			$('#forma-cotpoliticasdepago-window').find('#close').css({backgroundImage:"url(../../img/modalbox/close.png)"});
		});


		$('#forma-cotpoliticasdepago-window').find(".contenidoPes").hide(); //Hide all content
		$('#forma-cotpoliticasdepago-window').find("ul.pestanas li:first").addClass("active").show(); //Activate first tab
		$('#forma-cotpoliticasdepago-window').find(".contenidoPes:first").show(); //Show first tab content

		//On Click Event
		$('#forma-cotpoliticasdepago-window').find("ul.pestanas li").click(function() {
			$('#forma-cotpoliticasdepago-window').find(".contenidoPes").hide();
			$('#forma-cotpoliticasdepago-window').find("ul.pestanas li").removeClass("active");
			var activeTab = $(this).find("a").attr("href");
			$('#forma-cotpoliticasdepago-window').find( activeTab , "ul.pestanas li" ).fadeIn().show();
			$(this).addClass("active");
			return false;
		});

	}





	//nuevo politica da pago
	$new_cotpoliticasdepago.click(function(event){
		event.preventDefault();
		var id_to_show = 0;

		$(this).modalPanel_cotpoliticasdepago();

		var form_to_show = 'formacotpoliticasdepago';
		$('#' + form_to_show).each (function(){   this.reset(); });
		var $forma_selected = $('#' + form_to_show).clone();
		$forma_selected.attr({ id : form_to_show + id_to_show });

		$('#forma-cotpoliticasdepago-window').css({ "margin-left": -250, 	"margin-top": -200 });
		$forma_selected.prependTo('#forma-cotpoliticasdepago-window');
		$forma_selected.find('.panelcito_modal').attr({ id : 'panelcito_modal' + id_to_show , style:'display:table'});
		$tabs_li_funxionalidad();

		var $campo_id = $('#forma-cotpoliticasdepago-window').find('input[name=identificador]');
		var $campo_titulo = $('#forma-cotpoliticasdepago-window').find('input[name=titulo]');

		var $cerrar_plugin = $('#forma-cotpoliticasdepago-window').find('#close');
		var $cancelar_plugin = $('#forma-cotpoliticasdepago-window').find('#boton_cancelar');
		var $submit_actualizar = $('#forma-cotpoliticasdepago-window').find('#submit');

		$campo_id.attr({ 'value' : 0 });

		var respuestaProcesada = function(data){
			if ( data['success'] == "true" ){
				jAlert("La Politica fue dada de alta con &eacute;xito", 'Atencion!');
				var remove = function() { $(this).remove(); };
				$('#forma-cotpoliticasdepago-overlay').fadeOut(remove);
				//refresh_table();
				$get_datos_grid();
			}else{
				// Desaparece todas las interrogaciones si es que existen
				$('#forma-cotpoliticasdepago-window').find('div.interrogacion').css({'display':'none'});

				var valor = data['success'].split('___');
				//muestra las interrogaciones
				for (var element in valor){
					tmp = data['success'].split('___')[element];
					longitud = tmp.split(':');
					if( longitud.length > 1 ){
						$('#forma-cotpoliticasdepago-window').find('img[rel=warning_' + tmp.split(':')[0] + ']')
						.parent()
						.css({'display':'block'})
						.easyTooltip({	tooltipId: "easyTooltip2",content: tmp.split(':')[1] });
					}
				}
			}
		}
		var options = { dataType :  'json', success : respuestaProcesada };
		$forma_selected.ajaxForm(options);

		var input_json = document.location.protocol + '//' + document.location.host + '/'+controller+'/getCotPoliticasdePago.json';
		$arreglo = {'id':id_to_show};

		$.post(input_json,$arreglo,function(entry){
			//alert("Aqui no hay nada");
		},"json");//termina llamada json


		$cerrar_plugin.bind('click',function(){
			var remove = function() { $(this).remove(); };
			$('#forma-cotpoliticasdepago-overlay').fadeOut(remove);
		});

		$cancelar_plugin.click(function(event){
			var remove = function() { $(this).remove(); };
			$('#forma-cotpoliticasdepago-overlay').fadeOut(remove);
			$buscar.trigger('click');
		});




	});

	var carga_formacotpoliticasdepago_for_datagrid00 = function(id_to_show, accion_mode){
		//aqui entra para eliminar una entrada
		if(accion_mode == 'cancel'){

			var input_json = document.location.protocol + '//' + document.location.host + '/' + controller + '/' + 'logicDelete.json';
			$arreglo = {'id':id_to_show,
                                    'iu': $('#lienzo_recalculable').find('input[name=iu]').val()
                                    };
			jConfirm('Realmente desea eliminar la  Politica seleccionada', 'Dialogo de confirmacion', function(r) {
				if (r){
					$.post(input_json,$arreglo,function(entry){
						if ( entry['success'] == '1' ){
							jAlert("La Politica fue eliminada exitosamente", 'Atencion!');
							$get_datos_grid();
						}
						else{
							jAlert("La Politica no pudo ser eliminada", 'Atencion!');
						}
					},"json");
				}
			});

		}else{
			//aqui  entra para editar un registro
			var form_to_show = 'formacotpoliticasdepago';

			$('#' + form_to_show).each (function(){   this.reset(); });
			var $forma_selected = $('#' + form_to_show).clone();
			$forma_selected.attr({ id : form_to_show + id_to_show });

			$(this).modalPanel_cotpoliticasdepago();
			$('#forma-cotpoliticasdepago-window').css({ "margin-left": -350, 	"margin-top": -200 });

			$forma_selected.prependTo('#forma-cotpoliticasdepago-window');
			$forma_selected.find('.panelcito_modal').attr({ id : 'panelcito_modal' + id_to_show , style:'display:table'});

			$tabs_li_funxionalidad();

			var $campo_id = $('#forma-cotpoliticasdepago-window').find('input[name=identificador]');
			var $campo_titulo = $('#forma-cotpoliticasdepago-window').find('input[name=titulo]');

			var $cerrar_plugin = $('#forma-cotpoliticasdepago-window').find('#close');
			var $cancelar_plugin = $('#forma-cotpoliticasdepago-window').find('#boton_cancelar');
			var $submit_actualizar = $('#forma-cotpoliticasdepago-window').find('#submit');



			if(accion_mode == 'edit'){

				var input_json = document.location.protocol + '//' + document.location.host + '/'+controller+'/getCotPoliticasdePago.json';
				$arreglo = {'id':id_to_show};


				var respuestaProcesada = function(data){
					if ( data['success'] == 'true' ){
						var remove = function() { $(this).remove(); };
						$('#forma-cotpoliticasdepago-overlay').fadeOut(remove);
						jAlert("La Politica de Pago se ha actualizado.", 'Atencion!');
						$get_datos_grid();
					}
					else{
						// Desaparece todas las interrogaciones si es que existen
						$('#forma-cotpoliticasdepago-window').find('div.interrogacion').css({'display':'none'});

						var valor = data['success'].split('___');
						//muestra las interrogaciones
						for (var element in valor){
							tmp = data['success'].split('___')[element];
							longitud = tmp.split(':');
							if( longitud.length > 2 ){
								$('#forma-cotpoliticasdepago-window').find('img[rel=warning_' + tmp.split(':')[0] + ']')
								.parent()
								.css({'display':'block'})
								.easyTooltip({	tooltipId: "easyTooltip2",content: tmp.split(':')[1] });
							}
						}
					}
				}

				var options = {dataType :  'json', success : respuestaProcesada};
				$forma_selected.ajaxForm(options);

				//aqui se cargan los campos al editar
				$.post(input_json,$arreglo,function(entry){
					$campo_id.attr({ 'value' : entry['Politicas_Pago']['0']['id'] });
					$campo_titulo.attr({ 'value' : entry['Politicas_Pago']['0']['descripcion'] });
				},"json");//termina llamada json


				//Ligamos el boton cancelar al evento click para eliminar la forma
				$cancelar_plugin.bind('click',function(){
					var remove = function() { $(this).remove(); };
					$('#forma-cotpoliticasdepago-overlay').fadeOut(remove);
				});

				$cerrar_plugin.bind('click',function(){
					var remove = function() { $(this).remove(); };
					$('#forma-cotpoliticasdepago-overlay').fadeOut(remove);
					$buscar.trigger('click');
				});


			}
		}
	}

    $get_datos_grid = function(){
        var input_json = document.location.protocol + '//' + document.location.host + '/'+controller+'/getAllgetCotpoliticasdePago.json';

        var iu = $('#lienzo_recalculable').find('input[name=iu]').val();

        $arreglo = {'orderby':'id','desc':'DESC','items_por_pag':10,'pag_start':1,'display_pag':10,'input_json':'/'+controller+'/getAllgetcotpoliticasdepago.json', 'cadena_busqueda':$cadena_busqueda, 'iu':iu}

        $.post(input_json,$arreglo,function(data){

            //pinta_grid
            $.fn.tablaOrdenable(data,$('#lienzo_recalculable').find('.tablesorter'),carga_formacotpoliticasdepago_for_datagrid00);

            //resetea elastic, despues de pintar el grid y el slider
            Elastic.reset(document.getElementById('lienzo_recalculable'));
        },"json");
    }

    $get_datos_grid();


});
