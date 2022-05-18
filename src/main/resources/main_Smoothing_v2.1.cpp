/**
 * Copyright (c) 2015 Institut de recherches cliniques de Montreal (IRCM)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

//The core script is from Mathieu Blanchette, used in the paper:
//	Guillemette, B.*, Bataille, A.*, G�vry, N., Adam, M., Blanchette, Robert, F. and Gaudreau, L. (2005) Variant Histone H2A.Z is 
//	Globally Localized to the Promoters of Inactive Yeast Genes and Regulates Nucleosome Positioning. PLoS Biology 3(12), e384.
//After verification, the absolute maximal difference between results obtained from this script and the published smoothed results is 0.367,
//principally because there was a small error in the orginal version (j+=StepLength instead of j++).

#include <iostream>
#include <fstream>
#include <sstream>
#include <iomanip>
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>
#include <assert.h>	//�ventuellement remplacer les assert par des if(!)return ou exit pour l'int�ractivit�
#include <time.h>

using namespace std; //introduces namespace std

//----------------------------------D�CLARATIONS---------------------------------------
const int MAXrounds=10, MAXnbrChrom=25, MAXfloatMem=950000000;		//n�cessaires pour s'assurer que d�passera pas m�moire!
const int MAXwindowSize=10000, MAXnbrExtrm=50000, MAXnbrData=10000000;
const int MAXtempC=10000, NBRvalChqCote=3;

struct BED{
	string chromAlpha;	//pr tenir en compte certains noms de chrom alphanum
	int chromNum;
	int	debut;
	int fin;
	int centre;
	float valeur;
	void Init();
};

struct Chrom{
	string nom;
	int coordMin;
	int coordMax;
	int longueur;
};

int		ComparerFloat (const void*, const void*);
int		ComparerBED (const void*, const void*);

//----------------------------------MAIN---------------------------------------
int main(int argc, char *argv[]) {
	time_t tempDebut;
	struct tm * timeinfo;
	time ( &tempDebut );
	timeinfo = localtime ( &tempDebut );

	string nomFichierEntree, nomFichierSortie, datasetName, dbName;
	bool isCheckedSmooth, isCheckedMax, isCheckedMin;
	float stdDev, maxThreshold, minThreshold;
	int nbRounds, StepLength;
	char tempC[MAXtempC];
	if(argc==2){
		ifstream inParam(argv[1]);
		assert(inParam);
		if(inParam){
		    inParam.getline(tempC, MAXtempC, '\n');
		    nomFichierEntree=tempC;
		    inParam.getline(tempC, MAXtempC, '\n');
		    nomFichierSortie=tempC;
		    inParam.getline(tempC, MAXtempC, '\n');
		    datasetName=tempC;
		    inParam.getline(tempC, MAXtempC, '\n');
		    dbName=tempC;
			inParam>>stdDev>>nbRounds>>StepLength;
			inParam>>isCheckedSmooth>>isCheckedMax>>isCheckedMin>>maxThreshold>>minThreshold;
		    inParam.getline(tempC, MAXtempC, '\n');
		}
	}
	else if(argc==13){
	    nomFichierEntree=argv[1];
	    nomFichierSortie=argv[2];
	    datasetName=argv[3];
	    dbName=argv[4];
		stdDev=atof(argv[5]);
		nbRounds=atoi(argv[6]);
		StepLength=atoi(argv[7]);
		isCheckedSmooth=atoi(argv[8]);
		isCheckedMax=atoi(argv[9]);
		isCheckedMin=atoi(argv[10]);
		maxThreshold=atof(argv[11]);
		minThreshold=atof(argv[12]);
	}
	else{
		cout<<"Usage: Smoothing_exec [paramFileName or 12parameters...]"<<endl;
		cout<<"The 12 parameters of the file (one per line) or in the command line must be: inputFileName.wig outputFileName.bed datasetName UCSCdbName stdDev nbRounds stepLength isCheckedSmooth isCheckedMax isCheckedMin maxThreshold minThreshold"<<endl;
		cout<<"inputFileName.wig: inputFile in the wiggle format (chr start end value)"<<endl;
		cout<<"outputFileName.bed: outputFile with concatenated selected track(s)"<<endl;
		cout<<"datasetName: the name of the track"<<endl;
		cout<<"UCSCdbName: the UCSC assembly of the inputFile"<<endl;
		cout<<"stdDev: standard deviation of the gaussian used for the filter"<<endl;
		cout<<"nbRounds: Number of rounds of smoothing"<<endl;
		cout<<"stepLength: spacing between the middle coordinates of data in the output file"<<endl;
		cout<<"isCheckedSmooth, isCheckedMax and isCheckedMin: 1 to output this track, else 0"<<endl;
		cout<<"maxThreshold and minThreshold: the thresholds where local maximum/minimum (of 7 consecutive datapoints) is outputed "<<endl;
		exit(1);
	}

	//cout<<"nomFichierSortie="<<nomFichierSortie<<endl;
	//cout<<"nomFichierEntree="<<nomFichierEntree<<"_"<<endl<<"nomFichierSortie="<<nomFichierSortie<<"_"<<endl;
	//cout<<"nomFichierEntree="<<nomFichierEntree<<endl;
	//cout<<isCheckedSmooth<<isCheckedMax<<isCheckedMin<<maxThreshold<<minThreshold<<endl;
	assert(stdDev>0);
	int window_size=(int)(6*stdDev); //?�ventually param?
	assert(window_size<MAXwindowSize);
	assert(nbRounds>0 && nbRounds<MAXrounds);
	assert(StepLength>0);
//AJOUTER LIMITE datasetName AUX 64 PREMIERS CARACT SLMT PR �VITER OVERFLOW DE UCSC!!! ==> semble plut�t �tre limite ~115...



/*MB	FILE *f=fopen(argv[1],"r");
	assert(f);
	bool isCheckedReporters=atoi(argv[2]);
	bool isCheckedRatio=atoi(argv[3]);
	float stdDev=atof(argv[4]);			//default=200bp
	assert(stdDev>0);
	int window_size=(int)(6*stdDev); //?�ventually param?
	assert(window_size<MAXwindowSize);
	float ***bedSmooth;//[MAXrounds+1][MAXnbrChrom];	//bedSmooth[0]=raw_data
	int nbRounds=atoi(argv[5]);			//default=2
	assert(nbRounds>0 && nbRounds<MAXrounds);	//VERIF JAVASCRIPT
	bool isCheckedSmooth=atoi(argv[6]);
	bool isCheckedMax=atoi(argv[7]);
	bool isCheckedMin=atoi(argv[8]);
	int maxThreshold=atoi(argv[9]);		//default=1
	int minThreshold=atoi(argv[10]);	//default=-1
//	FILE *out1=fopen(argv[11],"w");
	ofstream out1;
	out1.open(argv[11]);
	assert(out1);
MB*/
	
	//s'assurer qu'au moins une piste de lissage est d�sir�e!!
	assert(isCheckedSmooth || isCheckedMax || isCheckedMin);


	//supporter les fichiers param cr��s autant win que unix
	ifstream inBed, inTest(nomFichierEntree.c_str());
	ofstream outBED;	//fichier poss�dant d'autres infos pour l'usager donc se positionner � la fin pour ajouter les donn�es smth
	string tempS1(""), tempS2(""), tempS3(""), tempS4("");
	if(inTest){
//		cout<<"fichier param format unix"<<endl;	
		inBed.open(nomFichierEntree.c_str());	
		outBED.open(nomFichierSortie.c_str(), ios_base::app);		//app	(append) Set the stream's position indicator to the end of the stream before each output operation
	}
	else{	
//		cout<<"fichier param format dos"<<endl;
		for(int i=0;i<nomFichierEntree.length()-1;i++)	tempS1+=nomFichierEntree[i];		//enlever le dernier caract�re dans nom de fichier
		for(int i=0;i<nomFichierSortie.length()-1;i++)	tempS2+=nomFichierSortie[i];		//enlever le dernier caract�re dans nom de fichier
//cout<<"nomFichierEntree="<<tempS1<<"_"<<endl<<"nomFichierSortie="<<tempS2<<"_"<<endl;
		inBed.open(tempS1.c_str());	
		outBED.open(tempS2.c_str(), ios_base::app);		//app	(append) Set the stream's position indicator to the end of the stream before each output operation
	    //traiter �galement ces deux noms
	    tempS3=datasetName;	    datasetName="";
		for(int i=0;i<tempS3.length()-1;i++)	datasetName+=tempS3[i];		//enlever le dernier caract�re dans nom de fichier
	    tempS4=dbName;	    dbName="";
		for(int i=0;i<tempS4.length()-1;i++)	dbName+=tempS4[i];		//enlever le dernier caract�re dans nom de fichier
	}
	assert(inBed);
	assert(outBED);
	inTest.close();

	
	//lire toutes les donn�es et les trier pour d�tecter le nbr de chr diff et la longueur � couvrir pour chq chrom (conserver min et max)
	BED *tabRawData;	int nbRawData(0);
	tabRawData = new BED [MAXnbrData];
	for(int a=0;a<MAXnbrData;a++)	tabRawData[a].Init();
	char c,h,r, tabTempC[MAXtempC];
	float tempF1, tempF2;
	bool estNum;

	inBed.getline(tempC, MAXtempC, '\n');
	while(inBed>>c>>h>>r){
		inBed>>tabRawData[nbRawData].chromAlpha>>tempF1>>tempF2>>tabRawData[nbRawData].valeur;
		tabRawData[nbRawData].debut=int(tempF1);
		tabRawData[nbRawData].fin=int(tempF2);
		tabRawData[nbRawData].centre=(tabRawData[nbRawData].debut+tabRawData[nbRawData].fin)/2;
		//v�rifier si chromosome num�rique ou alphanum�rique afin de trier en cons�quence
		estNum=true;
		for(int i=0;i<tabRawData[nbRawData].chromAlpha.length();i++){
			tabTempC[i]=tabRawData[nbRawData].chromAlpha[i];
			if(!isdigit(tabRawData[nbRawData].chromAlpha[i]))	estNum=false;
		}
		tabTempC[tabRawData[nbRawData].chromAlpha.length()]='\0';
		if(estNum)	tabRawData[nbRawData].chromNum=atoi(tabTempC);
		else tabRawData[nbRawData].chromNum=999;
		
		nbRawData++;
		assert(nbRawData<MAXnbrData);
	}
	inBed.close();
	cout<<"nbRawData="<<nbRawData<<" from "<<nomFichierEntree<<endl;
//	cout<<"tabRawData[0].chromAlpha="<<tabRawData[0].chromAlpha<<"   tabRawData[0].chromNum="<<tabRawData[0].chromNum<<endl;
//	cout<<"tabRawData[10000].chromAlpha="<<tabRawData[10000].chromAlpha<<"   tabRawData[10000].chromNum="<<tabRawData[10000].chromNum<<endl;
//	cout<<"tabRawData[nbRawData-1].chromAlpha="<<tabRawData[nbRawData-1].chromAlpha<<"   tabRawData[nbRawData-1].chromNum="<<tabRawData[nbRawData-1].chromNum<<endl;


	//trier tabRawData puis cr�er tabChrom (r�initialiser tabRawData[i].chromNum)
	Chrom *tabChrom = new Chrom[MAXnbrChrom];
	int nbrChrom(0), nbrNt(0), tempDeb;
	qsort(tabRawData, nbRawData, sizeof(BED), ComparerBED);
	
	tabChrom[nbrChrom].nom=tabRawData[0].chromAlpha;
//tabChrom[nbrChrom].coordMin=0;//pr comp H2AZ
	tempDeb=tabRawData[0].debut;
	while(tempDeb%10!=0 && tempDeb>0)	tempDeb--;	//pr initialiser la valeur d�but � min%10!!
	tabChrom[nbrChrom].coordMin=tempDeb;
	tabRawData[0].chromNum=nbrChrom;
	for(int i=1;i<nbRawData;i++){
		if(tabRawData[i].chromAlpha!=tabRawData[i-1].chromAlpha){
			//finaliser traitement chrom pr�c�dent
			tabChrom[nbrChrom].coordMax=tabRawData[i-1].fin;
			nbrNt+=tabChrom[nbrChrom].longueur=(tabChrom[nbrChrom].coordMax-tabChrom[nbrChrom].coordMin+1);
			nbrChrom++;
			//commencer traitement nouv chrom
			tabChrom[nbrChrom].nom=tabRawData[i].chromAlpha;
//		tabChrom[nbrChrom].coordMin=0;//pr comp H2AZ			
			tempDeb=tabRawData[i].debut;
			while(tempDeb%10!=0 && tempDeb>0)	tempDeb--;	//pr initialiser la valeur d�but � min%10!!
			tabChrom[nbrChrom].coordMin=tempDeb;			
		}
		tabRawData[i].chromNum=nbrChrom;
	}
	//finaliser traitement dernier chrom
	tabChrom[nbrChrom].coordMax=tabRawData[nbRawData-1].fin;
	nbrNt+=tabChrom[nbrChrom].longueur=(tabChrom[nbrChrom].coordMax-tabChrom[nbrChrom].coordMin+1);
	nbrChrom++;
	cout<<"nbrChrom="<<nbrChrom<<", nbrNt="<<nbrNt<<endl;
//	for(int b=0;b<nbrChrom;b++)	cout<<b<<" "<<tabChrom[b].nom<<" "<<tabChrom[b].coordMin<<" "<<tabChrom[b].coordMax<<" "<<tabChrom[b].longueur<<endl;
	
	
	//�valuer la qt� de m�moire n�cessaire et sortir du programme avec erreur -1 si trop demand�
	if(nbRounds*nbrNt>MAXfloatMem) return -1;
	//AM�LIORATIONS POTENTIELLES POUR DIMINUER CONSOMMATION DE M�MOIRE :
	//	- TRAITER UN CHR � LA FOIS (�QUIVALENT D'ENLEVER UNE DIMENSION � tabSmthData);
	//	- CONSERVER L'INFORMATION DE 2 RONDES SLMT (LA PR�SENTE ET LA PR�C�DENTE) ET FAIRE %2 POUR D�TERMINER QUEL INDICE UTILISER;
	//	- TRAITER LES DONNEES SOUS FORME DE "unsigned short"*10000 (�QUIVALENT DE PR�CISION 5) PLUT�T QUE FLOAT;
	//	- UTILISER COMPILATEUR et librairies 64 BITS POUR D�PASSER LA LIMITE DE 4Go!!!
	// 	- OU ENCORE CHANGER DE SERVEUR...


	//cr�er tabSmthData[nbRound][nbrChrom][tabChrom[nbrChrom].longueur] + bedMax/Min et TEMPORAIREMENT tabNbrData[nbrChrom][tabChrom[nbrChrom].longueur]
	float ***tabSmthData = new float**[nbRounds+1];		//nbRounds+1 car le 0 repr�sente initialisation
	for(int a=0;a<nbRounds+1;a++){
		tabSmthData[a] = new float*[nbrChrom];
		for(int b=0;b<nbrChrom;b++){
			tabSmthData[a][b] = new float[tabChrom[b].longueur];
			for(int c=0;c<tabChrom[b].longueur;c++){
				tabSmthData[a][b][c] = 0.0;
			}
		}
	}
	float **tabNbrData = new float*[nbrChrom];
	for(int b=0;b<nbrChrom;b++){
		tabNbrData[b] = new float[tabChrom[b].longueur];
		for(int c=0;c<tabChrom[b].longueur;c++){
			tabNbrData[b][c] = 0.0;
		}
	}
	BED *bedMax;	int nbrMax(0);	bool estMax;
	if(isCheckedMax){
		bedMax = new BED [MAXnbrExtrm];
		for(int a=0;a<MAXnbrExtrm;a++)	bedMax[a].Init();
	}

	BED *bedMin;	int nbrMin(0);	bool estMin;
	if(isCheckedMin){
		bedMin = new BED [MAXnbrExtrm];
		for(int a=0;a<MAXnbrExtrm;a++)	bedMin[a].Init();
	}
/*MB
	//cr�ation de bedSmooth[nbRounds][nbrChrom][nbrNtChrom]
	for (int a=0;a<=nbRounds;a++) {
		for (int i=0;i<MAXnbrChrom;i++) {
			bedSmooth[a][i]=(float*) malloc(MAXntChrom*sizeof(float));
			memset(bedSmooth[a][i],0,MAXntChrom*sizeof(float));
		}
	}
MB*/


	//remplir tabSmthData[nbRound][nbrChrom][tabChrom[nbrChrom].longueur] en utilisant tabNbrData pour calculer moyenne (utile seulement lors d'oligos chevauchant)
	for(int i=0;i<nbRawData;i++){
		for(int j=tabRawData[i].debut-tabChrom[tabRawData[i].chromNum].coordMin;j<tabRawData[i].fin-tabChrom[tabRawData[i].chromNum].coordMin;j++){
			tabSmthData[0][tabRawData[i].chromNum][j]+=tabRawData[i].valeur;
			tabNbrData[tabRawData[i].chromNum][j]++;
		}
	}
	//finaliser moyenne
	for(int b=0;b<nbrChrom;b++){
		for(int c=0;c<tabChrom[b].longueur;c++){
			if(tabNbrData[b][c])	tabSmthData[0][b][c]/=tabNbrData[b][c];
		}
	}
//cout<<"avant d�truire"<<endl;	
	//d�truire tab qui ne sont plus utiles
	for(int b=0;b<nbrChrom;b++)		delete[] tabNbrData[b];
//cout<<"1"<<endl;	
	delete tabNbrData;
//cout<<"2"<<endl;	
//	delete tabRawData;	???fait planter???
//cout<<"3"<<endl;	
	nbRawData=0;
	

/*MB	char chr[10];
	int ch,start,stop, nbrChrom(0);
	float x1;
	int maxPos[MAXnbrChrom];
//	cerr<<"Reading bed file"<<endl;
	char foo[10000];
	//ligne titre
	fscanf(f,"%[^\n]\n",foo);
PR�SENTEMENT L,ORDRE N,A PAS D,IMPORTANCE PUISQU,INITIALISATION DE CHQ POSITION DE FA�ON IND�PENDANTE!!
	while (fscanf(f,"chr%s %d %d %f\n",chr,&start,&stop,&x1)!=EOF) {
		//chrM= chrom 0
		if (chr[0]!='M') ch=atoi(chr); else ch=0;
TRAITER CHR DIFF CAR SI UN SEUL ET QUE C,EST 22 OU ENCORE CHR DROSO!!!
//		cerr<<ch<<" "<<start<<endl;
		assert(ch<MAXnbrChrom);
		if (nbrChrom<ch) nbrChrom=ch;
		//init chq nt d'un oligo � la valeur associ�e (round=0)(??lorsque oligos chevauchants??!!!=>N/A puisque fichier entr�e est un bed!!)
		for (int i=start;i<stop;i++) {
//		for (int i=start-30;i<stop+30;i++) {	//puisque oligo repr�sent� slmt par centre plut�t que long totale
			bedSmooth[0][ch][i]=x1;
MODIFIER DE SORTE � FAIRE MOYENNE LORSQUE CHEV!!!
			//conserver debut derni�re sonde chrom
			if (maxPos[ch]<start) maxPos[ch]=start;
		}
	}
//	cerr<<"Convolving"<<endl;

	//  fprintf(out2,"track type=wiggle_0 name=Max%s%d description=\"Max%s%d\" visibility=full color=200,0,50 autoScale=on maxHeightPixels=128:128:11 graphType=bar yLineMark=1 yLineOnOff=on windowingFunction=mean viewLimits=-5:5 gridDefault=on\n",argv[1],2,argv[1],2);
//	fprintf(out1,"track type=wiggle_0 name=smooth%s%d description=\"Smoothed%s%d \" visibility=full color=200,0,50 autoScale=on maxHeightPixels=128:128:11 graphType=point yLineMark=1 yLineOnOff=on windowingFunction=mean viewLimits=-3:3 gridDefault=on\n",argv[1],1,argv[1],1);
	out1<<"track type=wiggle_0 name=smth_"<<argv[1]<<" description=\"Smoothed_"<<argv[1]<<"\" visibility=full color=200,0,50 autoScale=on maxHeightPixels=64:64:11 graphType=point yLineMark=1 yLineOnOff=on windowingFunction=mean viewLimits=-3:3 gridDefault=on\n";
MB*/


	//init pond Gauss
//MB	float normal[MAXwindowSize];
	float *normal = new float[window_size];
	for (int i=0;i<window_size;i++) {
		normal[i]=1/sqrt(2*3.1416*stdDev*stdDev)*exp(-pow((i-window_size/2)/stdDev,2));
		//cerr<<i<<" "<<normal[i]<<endl;
	}


	//TRAITEMENT PRINCIPAL!!!
	double nbrOp(0), nbrOpTot;
	int nbrDixieme(0), demiStepLength, demiWindowSize;
	nbrOpTot=(nbRounds*nbrNt);
//cout<<"nbrOpTot="<<nbrOpTot<<endl;
	float moyennePonderee, sommePonderee, sommePonderation, valeurTemp;
	string finEnteteBED;
	finEnteteBED="visibility=full autoScale=off yLineMark=1 yLineOnOff=on windowingFunction=mean gridDefault=on priority=50 group=user db="+dbName+"\n";
	outBED<<"track type=wiggle_0 name=smth_"<<datasetName<<" description=\"Smoothed_"<<datasetName<<"__uncorrected_log2_ratio_SD=";
	outBED<<stdDev<<"_Rounds="<<nbRounds<<"_StepLength="<<StepLength<<"_"<<timeinfo->tm_year+1900<<"-"<<timeinfo->tm_mon+1<<"-"<<timeinfo->tm_mday;
	outBED<<"\" color=200,0,50 graphType=points viewLimits=-3:3 maxHeightPixels=128:64:16 "<<finEnteteBED;
	outBED<<setiosflags(ios::showpoint | ios::fixed)<<setprecision(4);
	demiStepLength=(StepLength/2);
	demiWindowSize=(window_size/2);
	//pr chq chrom
	for (int c=0;c<nbrChrom;c++) {
		//pr chq ronde
		for (int sm=1;sm<=nbRounds;sm++) {	//sm=1 et sm<=nbRounds car 0 �tait l'initialisation
			//d�part centre Gauss jusqu'� fin chrom-centre Gauss
//LE "i+=STEPlength" IMPLIQUE QUE LES DONN�ES DE LA RONDE PR�C�DENTE N'ONT PAS TOUTES �T� MISES � JOUR... V�RIF AUPR�S DE MB!	=>� REMPLACER PAR i++!!!
//			for (int i=demiWindowSize;i<tabChrom[c].longueur-demiWindowSize;i+=StepLength) {
			for (int i=demiWindowSize;i<tabChrom[c].longueur-demiWindowSize;i++) {
				if(100*nbrOp/nbrOpTot>=nbrDixieme*10){cout<<"\t"<<100*nbrOp/nbrOpTot<<"%"<<endl;	nbrDixieme++;}
				nbrOp++;
				sommePonderee=sommePonderation=0;
//?????				sommePonderation=normal[demiWindowSize];

				//pour largeur Gauss
				for (int j=0;j<window_size;j++) {
					//si valeur round pr�c!=0
					valeurTemp=tabSmthData[sm-1][c][i+j-demiWindowSize];
					if (valeurTemp!=0) {
						//sommePonderee+= valeur pond�r�e Gauss de chq fen�tre sur l'intervalle window_size!
						sommePonderee+=valeurTemp*normal[j];
						sommePonderation+=normal[j];
//test	if(sm==1 && c==0 && i==demiWindowSize)	outBED<<j<<"\t"<<valeurTemp<<"\t"<<valeurTemp*normal[j]<<"\t"<<sommePonderee<<"\t"<<normal[j]<<"\t"<<sommePonderation<<"\n";
					}
				}

				//finliser moyenne
				if (sommePonderation!=0) moyennePonderee=sommePonderee/sommePonderation;
				else moyennePonderee=0;
				
				tabSmthData[sm][c][i]=moyennePonderee;
//test	if(sm==1 && c==0 && i==demiWindowSize)	outBED<<moyennePonderee<<"\t"<<sommePonderee<<"\t"<<sommePonderation<<"\n";
			}
		}	//fin rounds

		if(isCheckedSmooth){
			//afficher r�sultats du dernier round, bons de STEPlength nt
			for (int i=demiWindowSize;i<tabChrom[c].longueur-demiWindowSize;i+=StepLength) {
				//<<setw(4);
				//sortie si coch�e
	//MB			fprintf(out1,"chr%d\t%d\t%d\t%f\n",c,i-5,i+5,bedSmooth[nbRounds][c][i]);
				if(tabSmthData[nbRounds][c][i]>0.00001 || tabSmthData[nbRounds][c][i]<(0-0.00001)){		//de sorte � afficher 0.0000 lorsque >0.00001 et <0.0001 (idem n�g), mais ne rien afficher si <=0.00001 pour ne pas occuper d'espace pour rien
					outBED<<"chr"<<tabChrom[c].nom<<"\t"<<i-demiStepLength+tabChrom[c].coordMin<<"\t"<<i+demiStepLength+tabChrom[c].coordMin<<"\t";
					outBED<<tabSmthData[nbRounds][c][i]<<"\n";
				}
			}
		}
		//analyse max si coch�e
		if(isCheckedMax){
			for (int i=demiWindowSize;i<tabChrom[c].longueur-demiWindowSize;i+=StepLength) {
				//si valeur > seuil
				if(tabSmthData[nbRounds][c][i]>=maxThreshold){
					estMax=true;
					//�liminer si trouve valeur plus grande parmi les NBRvalChqCote
					//�ventuellement � modifier pour remplacer par d�tection de plateau...
					for (int j=max(0,i-(NBRvalChqCote*StepLength));j<min(i+(NBRvalChqCote*StepLength),tabChrom[c].longueur);j+=StepLength)	if(tabSmthData[nbRounds][c][i]<tabSmthData[nbRounds][c][j]){estMax=false;	break;}
					if(estMax){
						bedMax[nbrMax].chromAlpha=tabChrom[c].nom;	bedMax[nbrMax].debut=i-demiStepLength+tabChrom[c].coordMin;	bedMax[nbrMax].fin=i+demiStepLength+tabChrom[c].coordMin;
						bedMax[nbrMax].valeur=tabSmthData[nbRounds][c][i];
						nbrMax++;
						assert(nbrMax<MAXnbrExtrm);
					}
				}
			}
		}
		//analyse min si coch�e
		if(isCheckedMin){
			for (int i=demiWindowSize;i<tabChrom[c].longueur-demiWindowSize;i+=StepLength) {
				//si valeur > seuil
				if(tabSmthData[nbRounds][c][i]<=minThreshold){
					estMin=true;
					//�liminer si trouve valeur plus grande parmi les NBRvalChqCote
					//�ventuellement � modifier pour remplacer par d�tection de plateau...
					for (int j=max(0,i-(NBRvalChqCote*StepLength));j<min(i+(NBRvalChqCote*StepLength),tabChrom[c].longueur);j+=StepLength)	if(tabSmthData[nbRounds][c][i]>tabSmthData[nbRounds][c][j]){estMin=false;	break;}
					if(estMin){
						bedMin[nbrMin].chromAlpha=tabChrom[c].nom;	bedMin[nbrMin].debut=i-demiStepLength+tabChrom[c].coordMin;	bedMin[nbrMin].fin=i+demiStepLength+tabChrom[c].coordMin;
						bedMin[nbrMin].valeur=tabSmthData[nbRounds][c][i];
						nbrMin++;
						assert(nbrMin<MAXnbrExtrm);
					}
				}
			}
		}
	}	//fin chrom
	outBED<<endl;

/*
//test contenu tabSmthData
outBED<<setiosflags(ios::showpoint | ios::fixed)<<setprecision(6);
for(int i=0;i<3100;i++)	outBED<<i<<"\t"<<tabChrom[0].nom<<"\t"<<i+tabChrom[0].coordMin<<"\t"<<tabSmthData[0][0][i]<<"\t"<<tabSmthData[1][0][i]<<"\t"<<tabSmthData[2][0][i]<<"\n";
//for(int i=0;i<3500;i++)	outBED<<i<<"\t"<<tabChrom[1].nom<<"\t"<<i+tabChrom[1].coordMin<<"\t"<<tabSmthData[0][1][i]<<"\t"<<tabSmthData[1][1][i]<<"\t"<<tabSmthData[2][1][i]<<"\n";
//for(int i=0;i<3500;i++)	outBED<<i<<"\t"<<tabChrom[2].nom<<"\t"<<i+tabChrom[2].coordMin<<"\t"<<tabSmthData[0][2][i]<<"\t"<<tabSmthData[1][2][i]<<"\t"<<tabSmthData[2][2][i]<<"\n";
//for (int i=0;i<window_size;i++)	outBED<<i<<"\t"<<normal[i]<<endl;
*/

/*MB
//traiter chq chr
  for (int c=0;c<20;c++) {
    cerr<<"chrom "<<c<<endl;
//pr chq round
	for (int sm=1;sm<=nbRounds;sm++) {
		cerr<<"Round "<<sm<<endl;
//d�part centre Gauss jusqu'� fin chrom, bons de 10nt
		for (int i=window_size/2;i<maxPos[c];i+=10) {
		  float x=0;
		  float n2=normal[window_size/2];
		  
//pour largeur Gauss
		  for (int j=0;j<window_size;j++) {
//si valeur round pr�c!=0
		    if (bedSmooth[sm-1][c][j+i-window_size/2]!=0) {
//x+= valeur pond�r�e Gauss de chq fen�tre intervalle window_size!
		      x+=bedSmooth[sm-1][c][j+i-window_size/2]*normal[j];
		      n2+=normal[j];
		      
		    }
		  }
		  
		  float y;
		  
//moyenne
		  if (n2!=0) y=x/n2;
		  else y=0;
		  
		  bedSmooth[sm][c][i]=y;
		}
    }	//fin round
    
//sortie des r�sultats du dernier round, m�mes bons de 10nt
	for (int i=window_size/2;i<maxPos[c];i+=10) {
	  fprintf(out1,"chr%d\t%d\t%d\t%f\n",c,i-5,i+5,bedSmooth[nbRounds][c][i]);
//si i-10>i-20 && i-10>i =>peak!
	  if (bedSmooth[nbRounds][c][i-10]-bedSmooth[nbRounds][c][i-20]>0 && bedSmooth[nbRounds][c][i-10]-bedSmooth[nbRounds][c][i]>0) {
	    fprintf(out2,"chr%d\t%d\t%d\t%f\n",c,i-15,i-5,bedSmooth[nbRounds][c][i-10]);
	  }
	}

  }	//fin chrom

MB*/	
	//si isCheckedMax => ajout d'une piste max
	if(isCheckedMax){
		outBED<<"track type=wiggle_0 name=max_"<<datasetName<<" description=\"Max_Smoothed_>"<<maxThreshold<<"_"<<datasetName<<"__uncorrected_log2_ratio_SD=";
		outBED<<stdDev<<"_Rounds="<<nbRounds<<"_StepLength="<<StepLength<<"_"<<timeinfo->tm_year+1900<<"-"<<timeinfo->tm_mon+1<<"-"<<timeinfo->tm_mday;
		outBED<<"\" color=0,150,50 graphType=bar viewLimits=0:3 maxHeightPixels=128:32:16 "<<finEnteteBED;
		for(int a=0;a<nbrMax;a++)	outBED<<"chr"<<bedMax[a].chromAlpha<<"\t"<<bedMax[a].debut<<"\t"<<bedMax[a].fin<<"\t"<<bedMax[a].valeur<<"\n";
	}

	//si isCheckedMin => ajout d'une piste min
	if(isCheckedMin){
		outBED<<"track type=wiggle_0 name=min_"<<datasetName<<" description=\"Min_Smoothed_<"<<minThreshold<<"_"<<datasetName<<"__uncorrected_log2_ratio_SD=";
		outBED<<stdDev<<"_Rounds="<<nbRounds<<"_StepLength="<<StepLength<<"_"<<timeinfo->tm_year+1900<<"-"<<timeinfo->tm_mon+1<<"-"<<timeinfo->tm_mday;
		outBED<<"\" color=50,50,200 graphType=bar viewLimits=-3:0 maxHeightPixels=128:32:16 "<<finEnteteBED;
		for(int a=0;a<nbrMin;a++)	outBED<<"chr"<<bedMin[a].chromAlpha<<"\t"<<bedMin[a].debut<<"\t"<<bedMin[a].fin<<"\t"<<bedMin[a].valeur<<"\n";
	}

	time_t tempFin;
	time ( &tempFin );
	timeinfo = localtime ( &tempFin );
//	cout<<"Current date and time are: "<< asctime (timeinfo)<<endl;
	cout<<tempFin-tempDebut<<" secondes pour tout le processus"<<endl;

return 0;
}


//----------------------------------D�FINITIONS---------------------------------------
///////////////////////////////////////////////////
//The function should receive two parameters (elem1 and elem2) that are pointers to elements, and should return 
//	an int value with the result of comparing them:
//QuickSort : http://www.cplusplus.com/ref/cstdlib/qsort.html
//return value	description
//		<0		*elem1 goes before *elem2
//		0		*elem1 == *elem2
//		>0		*elem1 goes after *elem2
///////////////////////////////////////////////////
/*int ComparerFloat (const void * a, const void * b){
  return ( *(float*)a - *(float*)b );
}*/
int ComparerFloat (const void * a, const void * b){
	float* e1 = (float*)a;
	float* e2 = (float*)b;
	if(*e1 < *e2) return -1;
	else if(*e1 > *e2) return 1;
	else return 0;
}

int	ComparerBED (const void * elem1, const void * elem2){
	BED* e1 = (BED*)elem1;
	BED* e2 = (BED*)elem2;

	//chromosome num�rique, ascendant
	if(e1->chromNum < e2->chromNum){return -1;}
	else if(e1->chromNum > e2->chromNum){return 1;}
	//chromosome alpha, ascendant
	if(e1->chromAlpha < e2->chromAlpha){return -1;}
	else if(e1->chromAlpha > e2->chromAlpha){return 1;}
	//si m�me chr:centre, ascendant
	else return (e1->centre - e2->centre);
}


///////////////////////////////////////////////////
void	BED::Init(){
	chromAlpha="";
	chromNum=debut=fin=centre=-1;
	valeur=0.0;
	
}
///////////////////////////////////////////////////
