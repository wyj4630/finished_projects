var cocktail = [ ["No.", "boisson", "prix($)"], [1, "Bicicletta", 10],[2, "Bourbon", 9],[3, "Ginger", 10],
             [4, "Cocktail", 10],[5, "Dark et Stormy", 9],[6, "Savoure", 8]
			 ];
var vin = [ ["No.", "boisson", "prix($)"],[1, "Masseto", 600], [2, "Sassicaia", 211], [3, "Tignanello", 241]
          ];
			 
var spirit = [ ["No.", "boisson", "prix($)"], [1, "Tequila", 30], [2, "Scotch", 25], [3, "Cognac", 45]
             ];
var beer = [ ["No.", "boisson", "prix($)"], [1, "Labatt", 6], [2, "Blonde", 6], [3, "Rousse", 8], [4, "Blanche", 5], [5, "Corona", 6]
           ];

var creerTable = function (tab, $id, name){
	var html = "<table id=\""+name+"Tab"+"\"><tr>";
	for (var i = 0; i < tab[0].length; i++){
		html += "<th class=\"t"+i+"\">"+tab[0][i]+"</th>";
	}
	html += "</tr>";
	for (var j = 1; j < tab.length; j++){
		html += "<tr>";
		for (i = 0; i < tab[j].length; i++){
			html += "<td class=\"t"+i+"\">"+tab[j][i]+"</td>";
		}
		html +="</tr>";
	}
	html +="</table>";
	$id.append(html);
};
var deleteTable = function ($tab){
    $tab.remove();
};

// chargement de la page
var hoverOn = function (tab, $id, name){
	$id.hover(function(){
		creerTable(tab, $(this), name);
	}, function(){
	    deleteTable($("#"+name+"Tab"));
	});
};

$(document).ready(function() {
	hoverOn (cocktail, $("#cocktail"), "cocktail"); 
	hoverOn (vin, $("#vin"), "vin"); 
	hoverOn (spirit, $("#spirit"), "spirit"); 
	hoverOn (beer, $("#beer"), "beer");
});
