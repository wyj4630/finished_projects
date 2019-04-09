//Fonction qui dessine un l'abyrinthe et le ressoud
var labySol = function(nx,ny,pas) {
    // creer un objet lab qui inclus l'ensembles mursH et mursV
    var lab = {mursH : iota(nx*(ny+1)), mursV : iota((nx+1)*ny)};
    creer(lab, nx, ny);
    dessiner(lab, nx, ny, pas);
    
    // S'assure que la tortue ne vois pas l'entre comme une sortie
    lab.mursH = ajouter(lab.mursH, 0);
    var pMursH = positionMursH(lab.mursH, nx, ny);
    var pMursV = positionMursV(lab.mursV, nx, ny);
    
    var p = {x : 0, y : 0}; //Position de la tortue
    var p1 = {x : 0, y : 0}; // Position supperflu pour dessiner sans modifier p
    
    var d = 3; //distance inverse entre le mur et la tortue 
    var i = 0;
    
    // Place la tortue à l'entrée du l'abyrinthe
    pu();
    mv(-(nx * pas)/2 + pas/3, (ny*pas)/2 + pas/d);
    pd();
    setpc(1,0,0);
    fd((d-1)*pas/d);
    
    // valide que le labyrinthe est bien resolue
    var terminer = false;
    do {
        terminer = false;
        
        // me donne un modulo positif de la direction (0 bas, 1 gauche, 2 haut, 3 droite)
        var direction = i % 4;
        direction = (direction + 4) % 4; 
        
        // verfie qu'il y a un mur pour que la tortue continue ou que i == 0
        if (i == 0 || (direction == 2 && prochainMurs(pMursV, p.x, p.y - 1) != null)
            || (direction == 3 && prochainMurs(pMursH, p.x - 1, p.y) != null)
            || (direction == 1 && prochainMurs(pMursH, p.x, p.y) != null)
            || (direction == 0 && prochainMurs(pMursV, p.x, p.y) != null)) {
                
            // verifie si la tortue va frapper un mur a la prochaine iteration
        	if ((direction == 0 && hitMurs(pMursH, p.x, p.y + 1) != null)
               || (direction == 2 && hitMurs(pMursH, p.x - 1, p.y - 1) != null)
               || (direction == 1 && hitMurs(pMursV, p.x + 1, p.y - 1) != null)
               || (direction == 3 && hitMurs(pMursV, p.x - 1, p.y) != null)) {
                
                avancer((d-2)*pas/d, p, direction);
                i++;
            }
            
            else {
                avancer(pas, p, direction);
                if (p.x == nx-1 && p.y == ny+1) {
                    terminer = true;
                }
            }
        }
        else {
            i--;
            avancer((2)*pas/d, p1, ((i%4) + 4) %4);
            terminer = true;
        }
    } while(!((p.x == nx-1) && (p.y == ny || p.y == ny+1)) || !terminer);
        
};

// Fonction qui avance la tortue du pas dans la direction
var avancer = function(pas, p, direction) {
    switch (direction) {
        case 0:
            fd(pas,0);
            p.y++;
            break;
        case 1:
            fd(0, pas);
            p.x++;
            break;
        case 2:
            bk(pas,0);
            p.y--;
            break;
        case 3:
            bk(0, pas);
            p.x--;
            break;
    }
};

// Fonction verifie si la tortue est en collision avec un mur selon la liste de murs et retourne ce mur
var hitMurs = function(murs, x, y) {
    var res = null;
    for (var i = 0; i < murs.length && res == null; i++) {
        if (x == murs[i].x && y == murs[i].y) {
            res = murs[i];
        }
    }
    return res;
};

// Fonction verifie si il y a un mur a suivre ou la tortue selon la liste de mur et retourne ce mur
var prochainMurs = function(murs, x, y) {
    var res = null;
    for (var i = 0; i < murs.length && res == null; i++) {
        if (x == murs[i].x && y == murs[i].y) {
            res = murs[i];
        }
    }
    return res;
};

// fonction qui retourne un tableau des points a droite des mursH
var positionMursH = function(mursH, nx, ny) {
    var res = Array(0);
    for (var i = 0; i < mursH.length; i++) {
        res.push(positionXY(mursH[i], nx, ny));
    }
    return res;
};

// fonction qui retourne un tableau des points en haut des mursV
var positionMursV = function(mursV, nx, ny) {
    var res = Array(0);
    for (var i = 0; i < mursV.length; i++) {
        res.push(positionXYV(mursV[i], nx, ny));
    }
    return res;
};

// Fonction qui creer un labyrinthe aleatoire
var laby = function (nx, ny, pas) {
    // creer un objet lab qui inclus l'ensembles mursH et mursV
    var lab = {mursH : iota(nx*(ny+1)), mursV : iota((nx+1)*ny)};
    creer(lab, nx, ny);
    dessiner(lab, nx, ny, pas);
};

// fonction qui dessine le labyrinthe
var dessiner = function (lab, nx, ny, pas) {
    rt(90);
    for (var i = 0; i < lab.mursH.length; i++) {
       pu();
       var p = positionXY(lab.mursH[i], nx, ny);
       mv(p.x * pas - (nx/2 * pas), (ny/2 * pas) - p.y * pas);
       pd();
       fd(pas);
    }
    rt(90);
    for (var i = 0; i < lab.mursV.length; i++) {
       pu();
       var p = positionXYV(lab.mursV[i], nx, ny);
       mv(p.x * pas - (nx/2 * pas), (ny/2 * pas) - p.y * pas);
       pd();
       fd(pas);
   }
};

// Fonction qui enleve des murs a lab pur creer un labyrinthe
var creer = function (lab, nx, ny) {
    // Enleve le mur de depart et d'arrive
   	lab.mursH = retirer(lab.mursH, 0);
    lab.mursH = retirer(lab.mursH, nx *(ny+1) - 1);
    
    //Choisi une premiere cave aléatoire
    var cave = [aleatoire0(nx*ny)];
    var positionCave = positionXY(cave[0], nx, ny);
    cave = ajouter(voisins(positionCave.x, positionCave.y, nx, ny), cave[0]);
    
    // Ajoute les voisins de la cave au front 
    var front = Array(0);
    for (var i = 0; i < cave.length; i++) {
        var position = positionXY(cave[i], nx, ny);
        var voisinsDeI = voisins(position.x, position.y, nx, ny);
        for (var x = 0; x < voisinsDeI.length - 1; x++) {
            if (!contient(cave, voisinsDeI[x])) {
                front = ajouter(front, voisinsDeI[x]);
            }
        }
    }
    
    //enleves les cotes de la premiere cavite
    var cotes = indices(positionCave.x, positionCave.y, nx, ny);
    for (var i = 0; i < cotes.length; i++) {
        if (cotes[i][0] == "H") {
            lab.mursH = retirer(lab.mursH, cotes[i][1]);
        }
        else {
            lab.mursV = retirer(lab.mursV, cotes[i][1]);
        }
    }
    
    //boucle qui enleve un cote jusqua ce que tout les elements sont dans la cave
    while (cave.length < nx*ny) {
        //Choisi une case et l'ajoute a la cave et l'enleve du front parmis le front
        var cavite = front[aleatoire0(front.length)];
        cave = ajouter(cave, cavite);
        front = retirer(front, cavite);
        
        //Choisi un cote adjacent à la cave et la nouvelle cavite au hasard à enlever
        var positionCave = positionXY(cavite, nx, ny);
        var listeVoisins = voisins(positionCave.x, positionCave.y, nx, ny);
    	var cotes = cotesAdjacent(cavite, positionCave.x, positionCave.y, nx, ny, cave);
        
        var n = aleatoire0(cotes.cellule.length); // le cote a enleve
        var enleve = {cellule : cotes.cellule[n], cote : cotes.cote[n]};
        if ((cavite - enleve.cellule) % nx == 0) {
            lab.mursH = retirer(lab.mursH, enleve.cote);
        }
        else {
            lab.mursV = retirer(lab.mursV, enleve.cote);
        }
 
        //Update la liste de front avec le nouvel element ajouter
        for (var x = 0; x < listeVoisins.length; x++) {
            if (!contient(cotes.cellule, listeVoisins[x])) {
                front = ajouter(front, listeVoisins[x]);
            }
        }
    }
};

// Fonction qui retourne les cotés qui sont partager avec la nouvelle cavité et la cave avec leur cellule
var cotesAdjacent = function (cavite, x, y, nx, ny, cave) {
    var res ={cellule : [], cote : []};
    // Verifie quel cellule de la cave est adjacent a la cavite 
    for (var i = 0; i < cave.length ; i++) {
        if (cave[i] - cavite == -nx ) {
            res.cote.push(x + y * nx);
            res.cellule.push(cave[i]);
        }
        else if (cave[i] - cavite == nx) {
            res.cote.push(x + (y+1) * nx);
            res.cellule.push(cave[i]);
        }
        else if (cave[i] - cavite == 1 && (cavite+1) % nx != 0) {
            res.cote.push(1 + x + y * (nx+1));
            res.cellule.push(cave[i]);
        }
        else if (cave[i] - cavite == -1 && cavite % nx != 0) {
            res.cote.push(x+y*(nx+1));
            res.cellule.push(cave[i]);
        }
    }
    return res;
};

// Fonction qui retourne l'indice des cotes non contours d'une cellule dans l'ordre n, s, e, o
var indices = function (x, y, nx, ny) {
   	var res = Array(0);
    var cellule = x+y*nx;
    if (!estContour("N", cellule, nx, ny)) {
        res.push(["H",cellule]);
    }
    if (!estContour("S", cellule, nx, ny)) {
        res.push(["H",x+(y+1)*nx]);
    }
    if (!estContour("E", cellule, nx, ny) ) {
        res.push(["V",1+x+y*(nx+1)]);
    }
   	if (!estContour("O", cellule, nx, ny)) {
        res.push(["V",x+y*(nx+1)]);
    }
    return res; 
};

// Fonction qui retourne la possition d'une cellule (horizontal) selon son numero de cellule
var positionXY = function (cellule, nx, ny) {
	return {x : (cellule % nx), y : (Math.floor(cellule / nx))};
};

// Fonction qui retourne la possition d'une cellule (verticale) selon son numero de cellule pour dessiner
var positionXYV = function (cellule, nx, ny) {
	return {x : (cellule % (nx + 1)), y : (Math.floor(cellule / (nx + 1)))};
};

// Fonction alleatoire qui retourne un nombre entre 0 et max - 1
var aleatoire0 = function (max) {
    return Math.floor(Math.random() * max);
};

// Fonction qui determine si un cote est un contour ou non
var estContour = function (cardinalite, cellule, nx, ny) {
    var res = false;
    switch (cardinalite) {
        case "N":
            res = cellule < nx; // vrai si cellule a completement en haut
            break;
        case "S":
            res = cellule >= (ny-1) * nx; // vrai si cellule a completement en bas
            break;
        case "E":
            res = ((cellule + 1) % nx == 0); // vrai si cellule a completement droite
            break;
        case "O":
            res = (cellule % nx == 0);
            break;
    }
    return res;
};

//fonction qui retourne un tableau de 0 a n-1
var iota = function(n) {
    var res = Array(n); // crée le tableau vide de longueur n
    // Ajoute les nombres de 1 a n
    for (var i = 0; i < n; i++) {
        res[i] = i;
    }
    return res;
};

//fonction qui determine si un element x est contenu dans tab
var contient = function(tab, x) {
	var res = false;
    // boucle qui verifie chaque element
    for (var i = 0; i < tab.length && !res; i++) {
        if (tab[i] == x) {
            res = true;
        }
    }
    return res;
};

//Fonction retourne un tableau qui ajoute x a tab a la fin si x n'est pas dans tab
var ajouter = function(tab, x) {
    var existe = false;
    
    //creer un tableau de longueur tab.length ou tab.length + 1 tout dependant si x existe
    var res = [];
   	
    for (var i = 0; i < tab.length; i++) {
        if (x == tab[i]) {
            existe = true;
        }
        res.push(tab[i]);
    }
    
    //Rajoute le x a la fin s'il n'est pas deja dans le tableau
   	if (!existe) {
        res.push(x);
    }
    
    return res;
};

// Fonction qui retourne un tableau equivalent a tab sans l'element x
var retirer = function(tab, x) {
    //creer un tableau qui va etre remplis de tous les elements sauf x
   	var res = Array(0);
    var indice = tab.length;
    for (var i = 0; i < tab.length && indice == tab.length; i++) {
        if (tab[i] == x) {
        	indice = i;
    	}
    }
    
    return indice == tab.length ? tab.slice(0,indice) : tab.slice(0, indice).concat(tab.slice(indice + 1, tab.length));
};

//Fonction qui retourne un tableau des voisins des cellule dans l'ordre croissant
var voisins = function(x, y, nx, ny) {
    //tableau initialement qui contirndra les voisins
    var res = Array(0);
    // Push la cellule si elle est dans le tableau
    if (y-1 >= 0) {
        res.push(x + (y-1) * (nx)); 
    }
    if (x-1 >= 0) {
        res.push((x-1) + y * (nx));
    }
    if (x+1 < nx) {
        res.push((x+1) + y * (nx));
    }
    if (y+1 < ny) {
        res.push(x + (y+1) * (nx));
    }
    return res;
};

var testEstContour = function() {
    assert(estContour("N", 0, 1, 2));
    assert(estContour("N", 3, 6, 5));
    assert(estContour("N", 5, 6, 5));
    assert(!estContour("N", 6, 6, 5));
    assert(!estContour("N", 14, 6, 5));
    
    assert(!estContour("S", 0, 8, 6));
    assert(estContour("S", 40, 10, 5));
    assert(estContour("S", 49, 10, 5));
    assert(estContour("S", 4, 1, 5));
    assert(!estContour("S", 15, 6, 7));
    
    assert(estContour("E", 4, 5, 1));
    assert(!estContour("E", 0, 5, 2));
    assert(!estContour("E", 7, 5, 2));
    assert(estContour("E", 11, 6, 2));
    
    assert(estContour("O", 0, 5, 1));
    assert(!estContour("O", 4, 5, 2));
    assert(!estContour("O", 7, 5, 2));
    assert(estContour("O", 12, 6, 3));
};

var testIota = function() {
    assert(iota(0).length == 0);
    assert(iota(1)[0] == 0);
    var tab = iota(10);
    
    for (var i = 0; i < 10; i++)
    assert(tab[i] == i);
};

var testContient = function() {
    assert(contient([], 0) == false);
    assert(contient([9,2,5], 2) == true);
	assert(contient([9,2,5], 4) == false);
};

var testAjouter = function() {
    assert(ajouter([], 0)[0] == 0);
    var tab = ajouter([9, 2, 5], 2);
    assert(tab.length == 3 && tab[0] == 9 && tab[1] == 2 && tab[2] == 5);
    var tab = ajouter([9, 2, 5], 4);
    assert(tab.length == 4 && tab[0] == 9 && tab[1] == 2 && tab[2] == 5 && tab[3] == 4); 
};

var testRetirer = function() {
    assert(retirer([], 0).length == 0);
    var tab = retirer([9, 2, 5], 2);
    assert(tab.length == 2 && tab[0] == 9 && tab[1] == 5);
    var tab = retirer([9, 2, 5], 4);
    assert(tab.length == 3 && tab[0] == 9 && tab[1] == 2 && tab[2] == 5); 
};

var testVoisins = function() {
    var tab = voisins(0, 0, 8, 4);
    assert(tab.length == 2 && tab[0] == 1 && tab[1] == 8);
    
    var tab = voisins(0, 1, 8, 4);
    assert(tab.length == 3 && tab[0] == 0 && tab[1] == 9 && tab[2] == 16);
    
    var tab = voisins(0, 4, 8, 5);
    assert(tab.length == 2 && tab[0] == 24 && tab[1] == 33);
    
    var tab = voisins(8, 0, 9, 10);
    assert(tab.length == 2 && tab[0] == 7 && tab[1] == 17);
    
    var tab = voisins(7, 3, 8, 4);
    assert(tab.length == 2 && tab[0] == 23 && tab[1] == 30);
    
    var tab = voisins(7, 2, 8, 4);
    assert(tab.length == 3 && tab[0] == 15 && tab[1] == 22 && tab[2] == 31);
    
    var tab = voisins(5, 0, 8, 4);
    assert(tab.length == 3 && tab[0] == 4 && tab[1] == 6 && tab[2] == 13);
    
    var tab = voisins(4, 3, 8, 4);
    assert(tab.length == 3 && tab[0] == 20 && tab[1] == 27 && tab[2] == 29);
    
    var tab = voisins(6, 2, 8, 4);
    assert(tab.length == 4 && tab[0] == 14 && tab[1] == 21 && tab[2] == 23 && tab[3] == 30);
    
    var tab = voisins(6, 2, 10, 6);
    assert(tab.length == 4 && tab[0] == 16 && tab[1] == 25 && tab[2] == 27 && tab[3] == 36);
};

var testHitMurs = function() {
    assert(hitMurs([], 4,4) == null);
    
    var mur = hitMurs([{x: 4, y : 4}], 4,4);
    assert(mur.x == 4 && mur.y == 4);
    
    var mur = hitMurs([{x: 4, y : 4}, {x : 1, y : 6}], 2,4);
    assert(mur== null);
    
    var mur = hitMurs([{x: 4, y : 4}, {x : 1, y : 6}], 1,6);
    assert(mur.x == 1 && mur.y == 6);
};

var testProchainMurs = function() {
    assert(hitMurs([], 4,4) == null);
    
    var mur = hitMurs([{x: 4, y : 4}], 4,4);
    assert(mur.x == 4 && mur.y == 4);
    
    var mur = hitMurs([{x: 4, y : 4}, {x : 1, y : 6}], 2,4);
    assert(mur== null);
    
    var mur = hitMurs([{x: 4, y : 4}, {x : 1, y : 6}], 1,6);
    assert(mur.x == 1 && mur.y == 6);
};

var testPositionMursH = function(mursH, nx, ny) {
    assert(positionMursH([], 4,4).length == 0);
    
    var murs = positionMursH([0], 4,4);
    assert(murs.length == 1 && murs[0].x == 0 && murs[0].y == 0);
    
    var murs = positionMursH([0, 2, 6], 4,4);
    assert(murs.length == 3 && murs[0].x == 0 && murs[0].y == 0
          && murs[1].x == 2 && murs[1].y == 0
          && murs[2].x == 2 && murs[2].y == 1);
};

var testPositionMursV = function(mursH, nx, ny) {
    assert(positionMursV([], 4,4).length == 0);
    
    var murs = positionMursV([0], 4,4);
    assert(murs.length == 1 && murs[0].x == 0 && murs[0].y == 0);
    
    var murs = positionMursV([0, 4, 5], 4,4);
    assert(murs.length == 3 && murs[0].x == 0 && murs[0].y == 0
          && murs[1].x == 4 && murs[1].y == 0
          && murs[2].x == 0 && murs[2].y == 1);
};

var testCotesAdjacent = function () {
    var cotes = cotesAdjacent(1, 1, 0, 8, 10, [0]);
    assert(cotes.cellule[0] == 0 && cotes.cote[0] == 1);
    
    var cotes = cotesAdjacent(8, 0, 1, 8, 10, [0]);
    assert(cotes.cellule[0] == 0 && cotes.cote[0] == 8);
    
    var cotes = cotesAdjacent(40, 0, 5, 8, 6, [0, 41, 39]);
    assert(cotes.cellule[0] == 41 && cotes.cote[0] == 46 && cotes.cote.length == 1);
    
    var cotes = cotesAdjacent(47, 7, 5, 8, 6, [0, 48,  15, 46, 39]);
    assert(cotes.cellule[1] == 39 && cotes.cote[1] == 47 && cotes.cote.length == 2);
    
    var cotes = cotesAdjacent(39, 7, 4, 8, 6, [0, 47,  15, 46, 38]);
    assert(cotes.cellule[0] == 47 && cotes.cote[0] == 47);
    
    var cotes = cotesAdjacent(5, 7, 4, 8, 6, [0, 47,  15, 46, 38]);
    assert(cotes.cellule.length == 0);
};

testCotesAdjacent();
testPositionMursV();
testPositionMursH();
testProchainMurs();
testHitMurs();
testEstContour();
testVoisins();
testAjouter();
testRetirer();
testContient();
testIota();