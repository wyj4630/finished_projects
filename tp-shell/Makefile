tp-shell/ch.c                                                                                       000644  000770  000024  00000000403 13413427134 013773  0                                                                                                    ustar 00lpaull                          staff                           000000  000000                                                                                                                                                                         /* ch.c.
auteur:
date:
problèmes connus:

  */

#include <stdio.h>
#include <stdlib.h>

int main (void)
{
  fprintf (stdout, "%% ");

  /* ¡REMPLIR-ICI! : Lire les commandes de l'utilisateur et les exécuter. */

  fprintf (stdout, "Bye!\n");
  exit (0);
}
                                                                                                                                                                                                                                                             tp-shell/rapport.tex                                                                                000644  000770  000024  00000000274 13413427134 015454  0                                                                                                    ustar 00lpaull                          staff                           000000  000000                                                                                                                                                                         \documentclass{article}

\usepackage[utf8]{inputenc}

\title{Travail pratique \#1 - IFT-2245}
\author{Moi et L'autre}

\begin{document}

\maketitle

%% ¡¡ REMPLIR ICI !!

\end{document}
                                                                                                                                                                                                                                                                                                                                    tp-shell/._tp-shell.tex                                                                             000644  000770  000024  00000000260 13417426701 015730  0                                                                                                    ustar 00lpaull                          staff                           000000  000000                                                                                                                                                                             Mac OS X            	   2   ~      �                                      ATTR       �   �                     �     com.apple.lastuseddate#PS    �+>\    ���)                                                                                                                                                                                                                                                                                                                                                    tp-shell/tp-shell.tex                                                                               000644  000770  000024  00000016020 13417426701 015514  0                                                                                                    ustar 00lpaull                          staff                           000000  000000                                                                                                                                                                         \documentclass{article}

\usepackage[utf8]{inputenc}
\usepackage{amsfonts}            %For \leadsto
\usepackage{amsmath}             %For \text
\usepackage{fancybox}            %For \ovalbox

\title{Travail pratique \#1}
\author{IFT-2245}

\begin{document}

\maketitle

{\centering \ovalbox{\large Dû le 8 février à 23h59} \\}

\newcommand \mML {\ensuremath\mu\textsl{ML}}
\newcommand \kw [1] {\textsf{#1}}
\newcommand \id [1] {\textsl{#1}}
\newcommand \punc [1] {\kw{`#1'}}
\newcommand \str [1] {\texttt{"#1"}}
\newenvironment{outitemize}{
  \begin{itemize}
  \let \origitem \item \def \item {\origitem[]\hspace{-18pt}}
}{
  \end{itemize}
}
\newcommand \Align [2][t] {
  \begin{array}[#1]{@{}l}
    #2
  \end{array}}

\section{Survol}

Ce TP vise à vous familiariser avec la programmation système dans un système
d'exploitation de style POSIX.
Les objectifs de ce travail sont les suivantes:
\begin{enumerate}
\item Parfaire sa connaissance de programmation en C.
\item Parfaire sa connaissance des ``forks'' et processus.
\end{enumerate}
Les étapes sont les suivantes:
\begin{enumerate}
\item Lire et comprendre cette donnée. 
\item Lire, trouver, et comprendre les parties importantes du code fourni.
\item Compléter le code fourni.
\item Écrire un rapport.  Il doit décrire \textbf{votre} expérience pendant
  les points précédents: problèmes rencontrés, surprises, choix que vous
  avez dû faire, options que vous avez sciemment rejetées, etc...  Le
  rapport ne doit pas excéder 5 pages.
\end{enumerate}

Ce travail est à faire en groupes de 2 étudiants.  Le rapport, au format
\LaTeX exclusivement et le code sont
à remettre par remise électronique avant la date indiquée.
Chaque jour de retard est -25\%.
Indiquez clairement votre nom au début de chaque fichier.

Si un étudiant préfère travailler seul, libre à lui, mais l'évaluation de
son travail n'en tiendra pas compte.  Si un étudiant ne trouve pas de
partenaire, il doit me contacter au plus vite.  Des groupes de 3 ou plus
sont \textbf{exclus}.

\newpage
\section{CH: un shell}

Vous allez devoir implanter une ligne de commande, similaire
à \texttt{/bin/sh}:
\begin{enumerate}
\item \textbf{Le Shell} -  être capable d'exécuter des commandes

\begin{verbatim}  
  $ ./ch
  
  votre shell> echo bonjour

  Output: bonjour
\end{verbatim}

Autre commandes que nous allons tester: (\texttt{cat, ls, man, tail}). NB: Tu ne doit pas faire les mises en oeuvre des fonctions mais juste les executer.

vous devez supposer que la commande a la structure:

\begin{verbatim}
[COMMAND_NAME] [arg1] [arg2] ... [-option_1] [-option_2] ...
\end{verbatim}

où le nombre d'arguments et / ou le nombre d'options peuvent être 0. Exemple:

\begin{verbatim}

  votre shell> ls -h (pas d'argument mais 1 option)
  votre shell> ls .. (1 argument mais pas d'option)

\end{verbatim} 


\item \textbf{Comprendre l'utilité du status de terminaison d'un processus avec $\&\&$ et $||$}:

\begin{verbatim}
  votre shell> cat nofile && echo Le fichier existe.

  Output:
  
  cat: nofile: No such file or directory

  votre shell> cat nofile || echo Le fichier n'existe pas

  Output:

  cat: nofile: No such file or directory
  Le fichier n'existe pas

\end{verbatim}
\newpage

\item \textbf{une déclaration ``IF''} - votre shell doit être  capable de faire:

\begin{verbatim}
  votre shell> if true ; do echo bonjour ; done

  Output:

  bonjour
\end{verbatim}
  De plus - il faut que votre ``IF'' fonctionne correctement avec le  $\&\&$ et $||$:

  
\begin{verbatim}
  votre shell> if cat nofile && true ; do echo Le fichier existe ; done

  Output:
  
  cat: nofile: No such file or directory

  votre shell> if cat nofile || true ; do echo Le fichier n'existe pas ; done

  Output:

  cat: nofile: No such file or directory
  Le fichier n'existe pas

\end{verbatim}
  
\item \textbf{Redirection de sortie et gestion de tache arriere-plan avec $\&$ et $>$}

 Mettre un \& à la fin de n'importe quelle instruction devrait renvoyer immédiatement l'utilisateur au shell et lui permettre d'entrer une autre commande. En d'autres termes, cette tâche devrait être mise en arrière-plan. Mais la sortie est toujours affichée. Exemple:

\begin{verbatim}
  votre shell> sleep 10 &
  [commence sleep 10, rien imprimé à l'écran]
  votre shell> echo blah
  blah
  votre shell> ...
  [après 10 seconde ... fini sleep 10]
\end{verbatim}

Mettre un $>$  à la fin de n'importe quelle instruction redirige la sortie vers un fichier. Exemple:

\begin{verbatim}
  votre shell> echo blah > blah.txt
\end{verbatim}
Aucun sortie et ``blah'' est ajouté à la fin de blah.txt (blah.txt est créé s'il n'existe pas)

Ces deux choses devraient également fonctionner ensemble. Exemple:
\begin{verbatim}
  votre shell> grep test / -r > output.txt &
\end{verbatim}

À noter: cela ne devrait pas avoir besoin de fonctionner pour les exécutables qui lisent depuis l'entrée standard. Exemple:
\begin{verbatim}
 votre shell>cat &
\end{verbatim}  


Bonus 1 (1 pt): Détecter le cas où \& est utilisé et que le processus attend une entrée standard et tuer le processus 

Bonus 2 (1 pt): Met un processus en arrière-plan pendant son exécution (et renvoie le shell à l'utilisateur) si l'utilisateur appuie sur le caractère ``CTRL-z''


À noter:
Les démonstrateur ne répondront pas aux questions sur les bonus

\end{enumerate}


Le programme doit être exécutable sur \texttt{arcade.iro} ou sur une nouvelle image de docker d'ubuntu.

Cela ne vous empêche pas bien sûr de le développer sur un système différent,
e.g. sous Windows avec Cygwin, mais assurez-vous que le résultat fonctionne
\emph{aussi} sur \texttt{arcade.iro}.

\section{Cadeaux}

Vous recevez en cadeau de bienvenue les fichiers \texttt{Makefile},
\texttt{rapport.tex}, et \texttt{ch.c} qui contiennent le squelette (vide)
du code et du rapport que vous devez rendre.

\subsection{Remise}

Pour la remise, vous devez remettre deux fichiers (\texttt{ch.c} et
\texttt{rapport.tex}) par la page  StudiUM du cours.
Assurez-vous que tout fonctionne correctement sur \texttt{arcade.iro}.

\section{Détails}

\begin{itemize}
\item La note sera divisée comme suit: 20\% pour chaque tache (1-4), et 20\% pour le rapport.
\item Tout usage de matériel (code ou texte) emprunté à quelqu'un d'autre
  (trouvé sur le web, ...) doit être dûment mentionné, sans quoi cela sera
  considéré comme du plagiat.
\item Le code ne doit en aucun cas dépasser 80 colonnes.
  indications supplémentaires.
\item La note sera basée d'une part sur des tests automatiques, d'autre part
  sur la lecture du code, ainsi que sur le rapport.  Le critère le plus
  important, et que votre code doit se comporter de manière correcte.
  Ensuite, vient la qualité du code: plus c'est simple, mieux c'est.
  S'il y a beaucoup de commentaires, c'est généralement un symptôme que le
  code n'est pas clair; mais bien sûr, sans commentaires le code (même
  simple) est souvent incompréhensible.  L'efficacité de votre code est sans
  importance, sauf s'il utilise un algorithme vraiment particulièrement
  ridiculement inefficace.
\end{itemize}

\end{document}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                