<?php

//ini_set('display_errors',1);
//ini_set('display_startup_erros',1);
error_reporting(E_ALL);
$charset = 'UTF-8';
mb_internal_encoding($charset);
setlocale(LC_ALL, 'pt_BR.'.$charset, 'pt_BR');
mb_language('uni');

//echo '<pre>';
$consulta = $_GET['c'];
$params = $_GET['p'];
if ($params == '' or is_null($params)) {
    $params = '';
} else {
    $params = split(';', $_GET['p']);
}
$ordem = $_GET['o'];


$sqls = [
  // 0 Pontuação até aproveitamento
  "select clu_nome, clu_estado, a.clu_ordem, " .
	              "       sum(pon_pontos) as pon_pontos, " .
	              "       sum(pon_jogos) as pon_jogos, " .
	              "       sum(pon_vitori) as pon_vitori, " .
	              "       sum(pon_empate) as pon_empate, " .
	              "       sum(pon_derrot) as pon_derrot, " .
	              "       sum(pon_golpro) as pon_golpro, " .
	              "       sum(pon_golcon) as pon_golcon, " .
	              "       sum(pon_saldo) as pon_saldo, " .
	              "       round(avg((pon_vitori * (case when pon_ano < 1995 then 2 else 3 end) + " .
                  "            pon_empate) * 100 / (pon_jogos * " .
	              "            (case when pon_ano < 1995 then 2 else 3 end))), 0) as pon_aprove " .
               	  "from PONTOS a, CLUBES b " .
                  "where a.tor_codigo = ? and " .
	              "      a.clu_ordem = b.clu_ordem and " .
	              "      pon_ano between ? and ? " .
	              "group by clu_nome, clu_estado, a.clu_ordem ",

  // 1 Pontuação de um ano só
  "select clu_nome, clu_estado, a.clu_ordem, " .
	              "       pon_pontos, pon_jogos, pon_vitori, " .
	              "       pon_empate, pon_derrot, pon_golpro, " .
	              "       pon_golcon, pon_saldo, pon_classi, " .
	              "       round((pon_vitori * (case when pon_ano < 1995 then 2 else 3 end) + " .
	              "            pon_empate) * 100 / (pon_jogos * " .
	              "            (case when pon_ano < 1995 then 2 else 3 end)), 0) as pon_aprove, " .
	              "       pon_observ, pon_artilh " .
	              "from PONTOS a, CLUBES b " .
	              "where a.tor_codigo = ? and " .
	              "      a.clu_ordem = b.clu_ordem and " .
	              "      pon_ano between ? and ? " .
   	              " order by pon_classi, pon_pontos desc, pon_jogos, pon_saldo desc, pon_golpro desc, clu_nome",
	
  // 2 Estado/país
  "select est_estado, " .
                  "       (case when d.tor_intern = 'S' then est_pais else b.clu_estado end) as clu_estado, " .
                  "       0 as ordem, " .
                  "       sum(pon_pontos) as pon_pontos, " .
                  "       sum(pon_jogos) as pon_jogos, " .
                  "       sum(pon_vitori) as pon_vitori, " .
                  "       sum(pon_empate) as pon_empate, " .
                  "       sum(pon_derrot) as pon_derrot, " .
                  "       sum(pon_golpro) as pon_golpro, " .
                  "       sum(pon_golcon) as pon_golcon, " .
                  "       sum(pon_saldo) as pon_saldo, " .
                  "       round(avg((pon_vitori * (case when pon_ano < 1995 then 2 else 3 end) + " .
                  "            pon_empate) * 100 / (pon_jogos * " .
                  "            (case when pon_ano < 1995 then 2 else 3 end))), 0) as pon_aprove " .
                  "from PONTOS a, CLUBES b, ESTADOS c, TORNEIOS d " .
                  "where a.tor_codigo = ? and " .
                  "      a.clu_ordem = b.clu_ordem and " .
                  "      b.clu_estado = c.clu_estado and " .
                  "      a.tor_codigo = d.tor_codigo and " .
                  "      pon_ano between ? and ? group by clu_estado, ordem " . // tinha est_estado antes
                  "order by pon_aprove desc, pon_jogos desc, pon_vitori desc, pon_saldo desc, pon_golpro desc",
				  
  // 3 Ranking 1o ao 10o
  "select clu_nome, clu_estado, a.clu_ordem, " .
                  "       sum(11 - pon_classi) as pon_classi " .
                  "from PONTOS a, CLUBES b " .
                  "where a.tor_codigo = ? and " .
                  "      a.clu_ordem = b.clu_ordem and " .
                  "      pon_classi < 11 and " .
                  "      pon_ano between ? and ? " .
                  "group by a.clu_ordem, clu_nome, clu_estado " .
                  "order by pon_classi desc, clu_nome",
					 
  // 4 Campeões
  "select clu_nome, clu_estado, a.clu_ordem, pon_pontos, pon_jogos, " .
                  "pon_vitori, pon_empate, pon_derrot, pon_golpro, pon_golcon, " .
                  "pon_saldo, pon_ano, " .
                  "       round((pon_vitori * (case when pon_ano < 1995 then 2 else 3 end) + " .
                  "            pon_empate) * 100 / (pon_jogos * " .
                  "            (case when pon_ano < 1995 then 2 else 3 end)), 0) as pon_aprove " .
                  "from PONTOS a, CLUBES b " .
                  "where a.tor_codigo = ? and " .
                  "      a.clu_ordem = b.clu_ordem and " .
                  "      pon_classi = 1 " .
                  "order by pon_ano",
  // 5 Maiores campeões
  "select clu_nome, clu_estado, a.clu_ordem, count(*) as pon_titulo " .
                  "from PONTOS a, CLUBES b " .
                  "where a.tor_codigo = ? and " .
                  "      a.clu_ordem = b.clu_ordem and " .
                  "      pon_classi = 1 and " .
                  "      pon_ano between ? and ? " .
                  "group by clu_nome, clu_estado, a.clu_ordem " .
                  "order by pon_titulo desc, clu_nome",
  // 6 Títulos por estado/país
  "select c.est_estado, " .
                  "   (case when d.tor_intern = 'S' then est_pais else b.clu_estado end) as est_pais, " .
                  "   count(*) as pon_titulo " .
                  "from PONTOS a, CLUBES b, ESTADOS c, TORNEIOS d " .
                  "where a.tor_codigo = ? and " .
                  "      a.clu_ordem = b.clu_ordem and " .
                  "      b.clu_estado = c.clu_estado and " .
                  "      a.tor_codigo = d.tor_codigo and " .
                  "      pon_classi = 1 and " .
                  "      pon_ano between ? and ? " .
                  "group by est_estado, est_pais " .
                  "order by pon_titulo desc, est_pais",
  // 7 Mais participações
  "select clu_nome, clu_estado, a.clu_ordem, count(*) as pon_titulo " .
                  "from PONTOS a, CLUBES b " .
                  "where a.tor_codigo = ? and " .
                  "      a.clu_ordem = b.clu_ordem and " .
                  "      pon_ano between ? and ? " .
                  "group by clu_nome, clu_estado, a.clu_ordem " .
                  "order by pon_titulo desc, clu_nome",
  // 8 Invictos
  "select clu_nome, clu_estado, pon_ano, pon_classi, a.clu_ordem " .
                  "from PONTOS a, CLUBES b " .
                  "where a.tor_codigo = ? and " .
                  "      a.clu_ordem = b.clu_ordem and " .
                  "      pon_derrot = 0 " .
                  "order by pon_ano, pon_classi",
  // 9 Artilheiros
  "select clu_nome, clu_estado, pon_ano, a.clu_ordem, pon_artilh " .
                  "from PONTOS a, CLUBES b " .
                  "where a.tor_codigo = ? and " .
                  "      a.clu_ordem = b.clu_ordem and " .
                  "      pon_artilh > ' ' " .
                  "order by pon_ano, pon_artilh desc",
  // 10 Maiores artilheiros
  "select clu_nome, clu_estado, pon_ano, a.clu_ordem, pon_artilh " .
                  "from PONTOS a, CLUBES b " .
                  "where a.tor_codigo = ? and " .
                  "      a.clu_ordem = b.clu_ordem and " .
                  "      pon_artilh > ' ' " .
                  "order by pon_artilh desc, pon_ano",
  // 11 Gols por Ano
  "select pon_ano, sum(pon_golpro) as pon_golpro, " .
                  "round(sum(pon_jogos) / 2) as pon_jogos, " .
                  "round(sum(pon_golpro) / sum(pon_jogos) * 2000, 0) as pon_media " .
                  "from PONTOS " .
                  "where tor_codigo = ? " .
                  "group by pon_ano " .
				  "order by pon_ano",
  // 12 Mais Gols por Ano
  "select pon_ano, sum(pon_golpro) as pon_golpro, " .
                  "round(sum(pon_jogos) / 2) as pon_jogos, " .
                  "round(sum(pon_golpro) / sum(pon_jogos) * 5000, 0) as pon_media " .
                  "from PONTOS " .
                  "where tor_codigo = ? " .
                  "group by pon_ano " .
				  "order by pon_golpro desc, pon_jogos, pon_ano",
  // 13 Média de Gols por Ano
  "select pon_ano, sum(pon_golpro) as pon_golpro, " .
                  "round(sum(pon_jogos) / 2) as pon_jogos, " .
                  "round(sum(pon_golpro) / sum(pon_jogos) * 5000, 0) as pon_media " .
                  "from PONTOS " .
                  "where tor_codigo = ? " .
                  "group by pon_ano " .
				  "order by pon_media desc, pon_golpro desc, pon_ano",
  // 14 Clube
  "select clu_nome, clu_estado, a.clu_ordem, pon_ano, " .
                  "       pon_classi, pon_pontos, pon_jogos, pon_vitori, " .
                  "       pon_empate, pon_derrot, pon_golpro, " .
                  "       pon_golcon, pon_saldo, " .
                  "       round((pon_vitori * (case when pon_ano < 1995 then 2 else 3 end) + " .
                  "                  pon_empate) * 100 / (pon_jogos * " .
                  "                 (case when pon_ano < 1995 then 2 else 3 end)), 0) as pon_aprove, " .
                  "       pon_observ, pon_artilh " .
                  "from PONTOS a, CLUBES b " .
                  "where a.tor_codigo = ? and " .
                  "      a.clu_ordem = b.clu_ordem and " .
                  "      clu_nome = ? and " .
                  "      clu_estado = ? " .
                  "order by pon_ano",
  // 15 Torneios
  "select tor_descri, tor_codigo from TORNEIOS order by tor_descri",
  // 16 Sigla do Torneio
  "select tor_codigo from TORNEIOS where tor_descri = ?",
  // 17 Anos do torneio
  "select distinct(pon_ano) as pon_ano " .
                  "from PONTOS where tor_codigo = ? " .
                  "order by pon_ano",
  // 18 Verificação se tem mais de um estado/país
  "select clu_estado, count(*) as num " .
                  "from PONTOS a, CLUBES b " .
                  "where a.clu_ordem = b.clu_ordem and tor_codigo = ?" .
                  " group by clu_estado",
  // 19 Clubes do torneio
  "select clu_nome, clu_estado " .
                  "from CLUBES c " .
                  "inner join PONTOS f on c.clu_ordem = f.clu_ordem and tor_codigo = ? " .
                  "order by clu_nome, clu_estado",
  // 20 Nome do estado/país
  "select est_estado from ESTADOS where clu_estado = ;",
  // 21 Número de clubes do torneio no ano
  "select count(*) as numero " .
                  "from PONTOS a, TORNEIOS b " .
                  "where b.tor_descri = ? and " .
                  "      a.tor_codigo = b.tor_codigo and " .
                  "      a.pon_ano = ?",
  // 22 Títulos do clube		
  "select pon_ano " .
                  "from PONTOS " .
                  "where tor_codigo = ? and" .
                  "      clu_ordem = ? and" .
                  "      pon_classi = 1 and" .
                  "      pon_ano between ? and ?" .
                  " order by pon_ano desc"
];

$ordens = [
  // 0 Pontuação
  "order by pon_pontos desc, pon_jogos, pon_saldo desc, pon_vitori desc, pon_golpro desc",
  
  // 1 Melhor Ataque
  "order by pon_golpro desc, pon_jogos, pon_golcon",
  
  // 2 Melhor Saldo de Gols
  "order by pon_saldo desc, pon_golpro desc, pon_jogos",
  
  // 3 Vitórias
  "order by pon_vitori desc, pon_saldo desc, pon_golpro desc, pon_jogos desc",
  
  // 4 Aproveitamento
  "order by pon_aprove desc, pon_jogos desc, pon_saldo desc, pon_golpro desc, pon_vitori desc"
];

if ($consulta > count($sqls) or $consulta < 0) {
	die('consulta inválida');
}
$sql = $sqls[intval($consulta)];


if (($ordem != '') and ($ordem >= 0) and ($ordem < count($ordens))) {
	$sql .= ' ' . $ordens[intval($ordem)];
}

$host="31.220.62.228";
$username="danielti_rank";
$password="U6WbLK2h";
$db_name="danielti_rank";
 
$connection = new mysqli($host, $username, $password, $db_name);
$connection->set_charset("utf8");

$types = ''; 

if ($params == '' or count($params) <= 0) {
//echo $sql . '<br/>';
//	echo "<br/>if param vazio<br/>";
	if ($result  = $connection->query($sql)) {
		
		//print_r($result);
		//echo "<br/>query<br/>";
		$results = array();
		while ($row = $result->fetch_assoc()) {
			$results[] = $row;
		}
		//echo "<br/>while<br/>";
		
		$result->close();
	}
}
else {

	
	foreach($params as $i => $param) {  
		if (is_numeric($param) and intval($param) == $param) {  
			$types .= 'i';              //integer
			$params[$i] = intval($param);
		} elseif (is_float($param)) {
			$types .= 'd';              //double
		} elseif (is_string($param)) {
			$types .= 's';              //string
		} else {
			$types .= 'b';              //blob and unknown
		}
	}
	array_unshift($params, $types);

	// Start stmt
	$query = $connection->stmt_init(); 
	//if() {
	$query->prepare($sql);

	//print_r($params);
		// Bind Params
		//call_user_func_array(array($query,'bind_param'),$params);
		if (count($params) == 2) {
			$query->bind_param($params[0], $params[1]);
		} else if (count($params) == 3) {
			$query->bind_param($params[0], $params[1], $params[2]);
		} else if (count($params) == 4) {
			$query->bind_param($params[0], $params[1], $params[2], $params[3]);
		} else if (count($params) == 5) {
			$query->bind_param($params[0], $params[1], $params[2], $params[3], $params[4]);
		} else if (count($params) == 6) {
			$query->bind_param($params[0], $params[1], $params[2], $params[3], $params[4], $params[5]);
		}

		$query->execute(); 

		// Get metadata for field names
		$meta = $query->result_metadata();

		// initialise some empty arrays
		$fields = $results = $fieldNames = array();

		// This is the tricky bit dynamically creating an array of variables to use
		// to bind the results
		while ($field = $meta->fetch_field()) {
			$var = $field->name;  
			$fieldNames[] = $var;
			$$var = null; 
			$fields[$var] = &$$var;
		}

		$fieldCount = count($fieldNames);

		// Bind Results     
		$result = call_user_func_array(array($query,'bind_result'), $fields);

		$i=0;
		while ($query->fetch()){
			for($l=0;$l<$fieldCount;$l++) 
				$results[$i][$fieldNames[$l]] = $fields[$fieldNames[$l]];
			$i++;
		}

    $query->close();
    // And now we have a beautiful
    // array of results, just like
    //fetch_assoc
	
}
header('Content-Type: application/json'); 
echo json_encode($results);
?>