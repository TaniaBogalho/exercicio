package com.exemplo.rest;

import com.exemplo.Dados;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;


@Path("/json")
public class JSONService {


	Date data = new Date();


	private static final DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	private static final DateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	private static Logger LOGGER = Logger.getLogger(JSONService.class.getName());

	FileHandler fileHandler;


	@POST
	@Path("/receiveJSON")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public JSONObject receiveJSON(JSONObject inputJsonObj) throws JSONException
	{

		try
		{
			//region ficheiro .log
			fileHandler = new FileHandler("/home/tania/logger.log", true);

			LOGGER.addHandler(fileHandler);
			SimpleFormatter formatter = new SimpleFormatter();
			fileHandler.setFormatter(formatter);
			//endregion

			//Escrevemos no ficheiro .log
			LOGGER.info("INICIO PROCESSO\n");

		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.log( Level.SEVERE, e.toString(), e );
		}

		Dados dados = new Dados();
		dados.setOp(inputJsonObj.getString("op"));
		dados.setValue1(Double.parseDouble(inputJsonObj.get("value1").toString()));
		dados.setValue2(Double.parseDouble(inputJsonObj.get("value2").toString()));


		//Escrevemos no ficheiro .log
		LOGGER.info("LEITURA DOS DADOS \nOP: " + dados.getOp() + "  Value1: " + dados.getValue1() + "  Value2: " + dados.getValue2() + "\n");

		//Escrevemos no ficheiro .log
		LOGGER.info(" FIM PROCESSO\n\n\n");

		//Chama a função clean para limpar os ficheiros .log.lck E .log0...x da diretoria
		clean(LOGGER);

		return dados.calcula();
	}



	//Função que limpa os Handlers (.log.lck, .log0...x)
	private void clean(Logger logger) {
		if (logger != null) {
			for (Handler handler : logger.getHandlers()) {
				handler.close();
			}
			clean(logger.getParent());
		}
	}





	//region CODIGO_ANTIGO
	/*
	@GET
	@Path("/sum/{value1}/{value2}")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getSumInJSON(@PathParam("value1") double value1, @PathParam("value2") double value2) throws JSONException {


		double total = value1 + value2;

		JSONObject obj = new JSONObject();
		obj.put("Total", total);
		obj.put("Data", sdf.format(data));

		return obj;
	}

	@GET
	@Path("/avg/{value1}/{value2}")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getAVGInJSON(@PathParam("value1") double value1, @PathParam("value2") double value2) throws JSONException {
		double total = (value1 + value2) / 2;

		JSONObject obj = new JSONObject();
		obj.put("Total", total);
		obj.put("Data", sdf.format(data));

		return obj;
	}

	@GET
	@Path("/mul/{value1}/{value2}")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getMulInJSON(@PathParam("value1") double value1, @PathParam("value2") double value2) throws JSONException {
		double total = value1 * value2;

		JSONObject obj = new JSONObject();
		obj.put("Total", total);
		obj.put("Data", sdf.format(data));

		return obj;
	}

	@GET
	@Path("/div/{value1}/{value2}")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getDivInJSON(@PathParam("value1") double value1, @PathParam("value2") double value2) throws JSONException {
		double total = value1 / value2;

		JSONObject obj = new JSONObject();
		obj.put("Total", total);
		obj.put("Data", sdf.format(data));

		return obj;
	}


	//Função que lê os dados recorrendo ao que é escrito no URL
	@GET
	@Path("/home/tania/input/{file}")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject CVSFileWEB(@PathParam("file") String file) throws IOException
	{

		//pw = new PrintWriter( new File("/home/tania/invalid/test_file_invalidos.csv"));

		File invalid_file = new File("/home/tania/invalid/test_file_invalidos.csv");
		fileWriter = new FileWriter(invalid_file, true);

		StringBuilder builder = new StringBuilder();
		String ColumnNamesList = "filename, op, value1, value2";



		fileHandler = new FileHandler("/home/tania/logger.log", true);
		LOGGER.addHandler(fileHandler);
		SimpleFormatter formatter = new SimpleFormatter();
		fileHandler.setFormatter(formatter);

		//Escrevemos no ficheiro .log
		LOGGER.info("INICIO PROCESSO LEITURA FICHEIRO " + file + " VIA URL\n");



		JSONObject obj = new JSONObject();
		JSONObject obj2 = new JSONObject();


		double total = 0;

		try
		{
			//Preenchemos o JSONObject obj com o que é retornado da função readCsvFile
			obj = cvs.readCsvFile("/home/tania/input/" + file);

			//Guardamos os valores lidos
			String op = obj.getString("op");
			double value1 = obj.getDouble("value1");
			double value2 = obj.getDouble("value2");


			//Verificamos que operação foi lida no ficheiro
			switch (op)
			{
				case "sum": total = value1 + value2;
					break;

				case "avg": total = (value1 + value2) / 2;
					break;

				case "mul": total = value1 * value2;
					break;

				case "div": if (value2 != 0)
								total = value1 / value2;
							else
								total = 0;
					break;

				default: total = 0;
						 op ="Operação Inválida";
					if (invalid_file.length() <= 1) {
						builder.append(ColumnNamesList + "\n");
						builder.append(file + ",");
						builder.append(op + ",");
						builder.append(value1 + ",");
						builder.append(value2 + ",");
						builder.append('\n');
						fileWriter.write(builder.toString());
						fileWriter.close();
					} else {
						builder.append(file + ",");
						builder.append(op + ",");
						builder.append(value1 + ",");
						builder.append(value2 + ",");
						builder.append('\n');
						fileWriter.write(builder.toString());
						fileWriter.close();
					}
					LOGGER.info("Erro no ficheiro lido.\n");


			}

			//Preenchemos o JSONObject obj2
			obj2.put("op", op);
			obj2.put("value1", value1);
			obj2.put("value2", value2);
			obj2.put("Total", total);
			obj2.put("Data", sdf.format(data));

			//Gravamos no ficheiro .log
			LOGGER.info(" LEITURA DOS DADOS\nOP: " + op + " VALUE1: " + value1 + " VALUE2: " + value2 + " Total: " + total + "\n");


			//Movemos o ficheiro tratado para a pasta output
			try {
				File source = new File("/home/tania/input/" + file);
				File dest = new File("/home/tania/output/" + file);

				source.renameTo(dest);


			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.log( Level.SEVERE, e.toString(), e );
			}


		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.log( Level.SEVERE, e.toString(), e );
		}


		//Escrevemos no ficheiro .log
		LOGGER.info(" FIM PROCESSO\n\n\n");


		//Chama a função clean para limpar os ficheiros .log.lck E .log0...x da diretoria
		clean(LOGGER);

		return obj2;
	}



	//Função que lê os dados ficheiro CSV colocado na diretoria input
	@GET
	@Path("/home/tania/input/")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject CVSFile() throws JSONException, IOException
	{
		//pw = new PrintWriter( new File("/home/tania/invalid/test_file_invalidos.csv"));

		File invalid_file = new File("/home/tania/invalid/test_file_invalidos.csv");
		fileWriter = new FileWriter(invalid_file,true);

		StringBuilder builder = new StringBuilder();
		String ColumnNamesList = "filename, op, value1, value2";

 		fileHandler = new FileHandler("/home/tania/logger.log", true);
		LOGGER.addHandler(fileHandler);
		SimpleFormatter formatter = new SimpleFormatter();
		fileHandler.setFormatter(formatter);

		java.nio.file.Path path = Paths.get("/home/tania/input/");

		JSONObject obj;
		JSONObject obj2 = new JSONObject();

		try
		{
			//Criamos um WatchService na diretoria que pretendemos observar
			WatchService watcher = path.getFileSystem().newWatchService();

			// associate watch service with the directory to listen to the event types
			path.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);

			// listen to events
			WatchKey watchKey = watcher.take();

			// get list of events as they occur
			List<WatchEvent<?>> events = watchKey.pollEvents();

			//iterate over events
			for (WatchEvent event : events)
			{
				//check if the event refers to a new file created
				if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE)
				{
					double total = 0;

					//Preenchemos o JSONObject com o que é retornado da função readCsvFile
					obj = cvs.readCsvFile("/home/tania/input/" + event.context().toString());

					//Escrevemos no ficheiro .log
					LOGGER.info("INICIO PROCESSO LEITURA DE FICHEIRO " + event.context().toString() + " QUANDO INSERIDO NUMA PASTA\n");


					//Guardamos os valores lidos no ficheiro
					String op = obj.getString("op");
					double value1 = obj.getDouble("value1");
					double value2 = obj.getDouble("value2");


					//Verificamos que operação foi lida no ficheiro
					switch (op)
					{
						case "sum": total = value1 + value2;
							break;

						case "avg": total = (value1 + value2) / 2;
							break;

						case "mul": total = value1 * value2;
							break;

						case "div": if (value2 != 0)
										total = value1 / value2;
									else
										total = 0;
							break;
						default: total = 0;
							op ="Operação Inválida";
							if (invalid_file.length() <= 1) {
								builder.append(ColumnNamesList + "\n");
								builder.append(event.context().toString() + ",");
								builder.append(op + ",");
								builder.append(value1 + ",");
								builder.append(value2 + ",");
								builder.append('\n');
								fileWriter.write(builder.toString());
								fileWriter.close();
							} else {
								builder.append(event.context().toString() + ",");
								builder.append(op + ",");
								builder.append(value1 + ",");
								builder.append(value2 + ",");
								builder.append('\n');
								fileWriter.write(builder.toString());
								fileWriter.close();
							}
							LOGGER.info("Erro no ficheiro lido.\n");

					}

					//Preenchemos o JSONObject
					obj2.put("op", op);
					obj2.put("value1", value1);
					obj2.put("value2", value2);
					obj2.put("Total", total);
					obj2.put("Data", sdf.format(data));


					//Escrevemos no ficheiro .log
					LOGGER.info(" LEITURA DOS DADOS\nOP: " + op + " VALUE1: " + value1 + " VALUE2: " + value2 + " Total: " + total + "\n");


					//Movemos o ficheiro tratado para a pasta output
					try {

						File source = new File("/home/tania/input/" + event.context().toString());
						File dest = new File("/home/tania/output/" + event.context().toString());

						source.renameTo(dest);

					}
					catch(Exception e){
						e.printStackTrace();
						LOGGER.log( Level.SEVERE, e.toString(), e );
					}
				}
			}

			//Fechamos o watcher
			watcher.close();
		}
		catch (IOException e) {
			e.printStackTrace();
			LOGGER.log( Level.SEVERE, e.toString(), e );
		}
		catch (InterruptedException e) {
			e.printStackTrace();
			LOGGER.log( Level.SEVERE, e.toString(), e );
		}

		//Escrevemos no ficheiro .log
		LOGGER.info(" FIM PROCESSO\n\n\n");


		//Chama a função clean para limpar os ficheiros .log.lck E .log0...x da diretoria
		clean(LOGGER);

		return obj2;
	}*/
	//endregion
}




