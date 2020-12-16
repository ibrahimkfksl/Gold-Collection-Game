package backend;

import anamenu.OyunEkrani;
import javafx.scene.paint.Color;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class OyuncuB {

	private int bulunan_nokta[];// (x,y)seklinde tutuluyor
	private int hedef[];// (x,y) seklinde tutuluyor
	private int toplam_altin;
	private int adim_maliyeti;
	private int hedef_belirleme_maliyeti;
	private int adim_sayisi;

	private int atilan_adim_sayisi;
	private int harcanan_altin;
	private int toplanan_altin_miktari;

	private File cikti;
	private BufferedWriter br;
	private Boolean elendi_mi;

	private OyuncuA oyuncu_a;
	private OyuncuC oyuncu_c;
	private OyuncuD oyuncu_d;
	private Boolean adim_eksilt;

	private int masa_x;
	private int masa_y;

	private OyunMasasi oyun_masasi;

	public OyuncuB(int toplam_altin, OyunMasasi oyun_masasi, int adim_maliyeti, int hedef_belirleme_maliyeti,
			int adim_sayisi) {
		this.toplam_altin = toplam_altin;
		this.oyun_masasi = oyun_masasi;
		this.adim_maliyeti = adim_maliyeti;
		this.hedef_belirleme_maliyeti = hedef_belirleme_maliyeti;
		this.adim_sayisi = adim_sayisi;

		masa_x = oyun_masasi.getX();
		masa_y = oyun_masasi.getY();

		hedef = new int[2];
		hedef[0] = -1;
		hedef[1] = -1;

		bulunan_nokta = new int[2];
		bulunan_nokta[0] = masa_x - 1;
		bulunan_nokta[1] = 0;
		OyunEkrani.board[bulunan_nokta[1]][bulunan_nokta[0]].setFill(Color.GRAY);

		atilan_adim_sayisi = 0;
		harcanan_altin = 0;
		toplanan_altin_miktari = 0;

		elendi_mi = false;
		cikti = new File("Oyuncu_B.txt");
		BufferedWriter br2;
		try {
			br2 = new BufferedWriter(new FileWriter(cikti));
			br = br2;
			br.write("----------Oyuncu B----------");
			br.newLine();
			br.write("Baslangic Altini                   : " + toplam_altin);
			br.newLine();
			br.write("Hedef Belirleme Maliyeti           : " + hedef_belirleme_maliyeti);
			br.newLine();
			br.write("Adim Atmanin Maliyeti              : " + adim_maliyeti);
			br.newLine();
			br.write("Tek Seferde Gidilebilen Adim Sayisi: " + adim_sayisi);
			br.newLine();
			br.write("-------------------------------------------------------");
			br.newLine();

		} catch (IOException e) {
			System.out.println("B Oyuncusunda Dosyaya Yazma hatasi");

		}

	}

	public void hedefBelirle() {

		int gecici_hedef[] = new int[2];
		gecici_hedef[0] = -1;
		gecici_hedef[1] = -1;

		int mesafe = masa_x + masa_y;
		int kazanc = (mesafe * -1) * adim_maliyeti;// buna farkli bir deger de atanabilir sayi cok kucuk oldugu surece
													// sikint yok

		for (int i = 0; i < masa_y; i++) {
			for (int j = 0; j < masa_x; j++) {

				if (oyun_masasi.getMasa()[i][j] > 0) {

					int yeni_mesafe = (Math.abs(bulunan_nokta[0] - j) + Math.abs(bulunan_nokta[1] - i));
					int yeni_kazanc = oyun_masasi.getMasa()[i][j] - (yeni_mesafe * adim_maliyeti);

					//
					if (yeni_kazanc > kazanc) {
						gecici_hedef[0] = j;
						gecici_hedef[1] = i;
						kazanc = yeni_kazanc;
					}
				}

			}
		}

		hedef[0] = gecici_hedef[0];
		hedef[1] = gecici_hedef[1];

	}

	/*
	 * HareketEt metodu bir oyuncunun sirasi geldiginde yapmasi gereken tum
	 * durumlari yapiyor Hedefi yok ise hedef buluyor.Hedefi var ise hedefe dogru
	 * hareket ediyor Yaptigi hareketlere gore de maliyetini toplam altin
	 * bakiyesinden dusuyor Eger bulundugu yerde altin varsa da bu altini
	 * aliyor(hedefindeki altini) Eger gittigi yolda da gizli altin varsa bu altini
	 * aciga cikariyor.
	 *
	 * return==1 ise altin bitmistir. Elenmistir. return==2 ise bu tur hedef
	 * bulunamamistir. Bir sonraki tur icin beklenecektir return==3 ise oyuncu
	 * hareket etmistir. Sira bir sonraki oyuncudadir
	 */
	public int hareketEt() {

		if ((hedef[0] == -1) || !hedefKontrolEt(hedef[0], hedef[1])) {
			if (toplam_altin < hedef_belirleme_maliyeti) {
				eleniceDosyayaYaz();
				return 1;
			} else {
				hedefBelirle();
				yeniHedefiDosyayaYaz();
			}

			if ((hedef[0] == -1) && (hedef[1] == -1)) {
				return 2;
			} else {
				toplam_altin -= hedef_belirleme_maliyeti;
				harcanan_altin += hedef_belirleme_maliyeti;
			}
		}

		int yon = 1;// 1 de x yonunda 2 de y yonunde hareket ediyor

		hareketOncesiKonumuYazdir();
		for (int i = 0; i < adim_sayisi; i++) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (toplam_altin < adim_maliyeti) {
				eleniceDosyayaYaz();
				return 1;
			}
			adim_eksilt = false;
			if (yon == 1) {
				if (hedef[0] != bulunan_nokta[0]) {
					if (hedef[0] < bulunan_nokta[0]) {
						if (!oyuncuOnumdeMi(bulunan_nokta[0] - 1, bulunan_nokta[1])) {
							OyunEkrani.board[bulunan_nokta[1]][bulunan_nokta[0]].setFill(Color.WHITE);
							bulunan_nokta[0] -= 1;
							OyunEkrani.board[bulunan_nokta[1]][bulunan_nokta[0]].setFill(Color.GRAY);
							if (oyun_masasi.getMasa()[bulunan_nokta[1]][bulunan_nokta[0]] > 0) {
								toplam_altin += oyun_masasi.getMasa()[bulunan_nokta[1]][bulunan_nokta[0]];
								oyun_masasi.getMasa()[bulunan_nokta[1]][bulunan_nokta[0]] = 0;
								toplam_altin += oyun_masasi.getMasa()[bulunan_nokta[1]][bulunan_nokta[0]];

							}
							toplam_altin -= adim_maliyeti;
							harcanan_altin += adim_maliyeti;
							atilan_adim_sayisi++;
							gidilenYoluYazdir(oyun_masasi.gizliAltinOrtayaCikar(bulunan_nokta[0], bulunan_nokta[1]),
									bulunan_nokta[0], bulunan_nokta[1]);
						} else {
							OyunEkrani.board[bulunan_nokta[1]][bulunan_nokta[0]].setFill(Color.WHITE);

							yon = yonDegistir(yon);
							if (adim_eksilt == true)
								i--;
							OyunEkrani.board[bulunan_nokta[1]][bulunan_nokta[0]].setFill(Color.GRAY);
							if (oyun_masasi.getMasa()[bulunan_nokta[1]][bulunan_nokta[0]] > 0) {
								toplam_altin += oyun_masasi.getMasa()[bulunan_nokta[1]][bulunan_nokta[0]];
								oyun_masasi.getMasa()[bulunan_nokta[1]][bulunan_nokta[0]] = 0;
								toplam_altin += oyun_masasi.getMasa()[bulunan_nokta[1]][bulunan_nokta[0]];

							}

						}

					} else if (hedef[0] > bulunan_nokta[0]) {
						if (!oyuncuOnumdeMi(bulunan_nokta[0] + 1, bulunan_nokta[1])) {
							OyunEkrani.board[bulunan_nokta[1]][bulunan_nokta[0]].setFill(Color.WHITE);
							bulunan_nokta[0] += 1;
							OyunEkrani.board[bulunan_nokta[1]][bulunan_nokta[0]].setFill(Color.GRAY);
							if (oyun_masasi.getMasa()[bulunan_nokta[1]][bulunan_nokta[0]] > 0) {
								toplam_altin += oyun_masasi.getMasa()[bulunan_nokta[1]][bulunan_nokta[0]];
								oyun_masasi.getMasa()[bulunan_nokta[1]][bulunan_nokta[0]] = 0;
								toplam_altin += oyun_masasi.getMasa()[bulunan_nokta[1]][bulunan_nokta[0]];

							}
							toplam_altin -= adim_maliyeti;
							harcanan_altin += adim_maliyeti;
							atilan_adim_sayisi++;
							gidilenYoluYazdir(oyun_masasi.gizliAltinOrtayaCikar(bulunan_nokta[0], bulunan_nokta[1]),
									bulunan_nokta[0], bulunan_nokta[1]);
						} else {
							OyunEkrani.board[bulunan_nokta[1]][bulunan_nokta[0]].setFill(Color.WHITE);

							yon = yonDegistir(yon);
							if (adim_eksilt == true)
								i--;
							OyunEkrani.board[bulunan_nokta[1]][bulunan_nokta[0]].setFill(Color.GRAY);
							if (oyun_masasi.getMasa()[bulunan_nokta[1]][bulunan_nokta[0]] > 0) {
								toplam_altin += oyun_masasi.getMasa()[bulunan_nokta[1]][bulunan_nokta[0]];
								oyun_masasi.getMasa()[bulunan_nokta[1]][bulunan_nokta[0]] = 0;
								toplam_altin += oyun_masasi.getMasa()[bulunan_nokta[1]][bulunan_nokta[0]];

							}

						}

					}
				}

				if (hedef[0] == bulunan_nokta[0]) {
					yon = 2;

				}

			}

			else if (yon == 2) {
				if (hedef[1] != bulunan_nokta[1]) {
					if (hedef[1] < bulunan_nokta[1]) {
						if (!oyuncuOnumdeMi(bulunan_nokta[0], bulunan_nokta[1] - 1)) {
							OyunEkrani.board[bulunan_nokta[1]][bulunan_nokta[0]].setFill(Color.WHITE);
							bulunan_nokta[1] -= 1;
							OyunEkrani.board[bulunan_nokta[1]][bulunan_nokta[0]].setFill(Color.GRAY);
							if (oyun_masasi.getMasa()[bulunan_nokta[1]][bulunan_nokta[0]] > 0) {
								toplam_altin += oyun_masasi.getMasa()[bulunan_nokta[1]][bulunan_nokta[0]];
								oyun_masasi.getMasa()[bulunan_nokta[1]][bulunan_nokta[0]] = 0;
								toplam_altin += oyun_masasi.getMasa()[bulunan_nokta[1]][bulunan_nokta[0]];

							}
							toplam_altin -= adim_maliyeti;
							harcanan_altin += adim_maliyeti;
							atilan_adim_sayisi++;
							gidilenYoluYazdir(oyun_masasi.gizliAltinOrtayaCikar(bulunan_nokta[0], bulunan_nokta[1]),
									bulunan_nokta[0], bulunan_nokta[1]);
						} else {
							OyunEkrani.board[bulunan_nokta[1]][bulunan_nokta[0]].setFill(Color.WHITE);

							yon = yonDegistir(yon);
							if (adim_eksilt == true)
								i--;
							OyunEkrani.board[bulunan_nokta[1]][bulunan_nokta[0]].setFill(Color.GRAY);
							if (oyun_masasi.getMasa()[bulunan_nokta[1]][bulunan_nokta[0]] > 0) {
								toplam_altin += oyun_masasi.getMasa()[bulunan_nokta[1]][bulunan_nokta[0]];
								oyun_masasi.getMasa()[bulunan_nokta[1]][bulunan_nokta[0]] = 0;
								toplam_altin += oyun_masasi.getMasa()[bulunan_nokta[1]][bulunan_nokta[0]];

							}

						}

					} else if (hedef[1] > bulunan_nokta[1]) {
						if (!oyuncuOnumdeMi(bulunan_nokta[0], bulunan_nokta[1] + 1)) {
							OyunEkrani.board[bulunan_nokta[1]][bulunan_nokta[0]].setFill(Color.WHITE);
							bulunan_nokta[1] += 1;
							OyunEkrani.board[bulunan_nokta[1]][bulunan_nokta[0]].setFill(Color.GRAY);
							if (oyun_masasi.getMasa()[bulunan_nokta[1]][bulunan_nokta[0]] > 0) {
								toplam_altin += oyun_masasi.getMasa()[bulunan_nokta[1]][bulunan_nokta[0]];
								oyun_masasi.getMasa()[bulunan_nokta[1]][bulunan_nokta[0]] = 0;
								toplam_altin += oyun_masasi.getMasa()[bulunan_nokta[1]][bulunan_nokta[0]];

							}
							toplam_altin -= adim_maliyeti;
							harcanan_altin += adim_maliyeti;
							atilan_adim_sayisi++;
							gidilenYoluYazdir(oyun_masasi.gizliAltinOrtayaCikar(bulunan_nokta[0], bulunan_nokta[1]),
									bulunan_nokta[0], bulunan_nokta[1]);
						} else {
							OyunEkrani.board[bulunan_nokta[1]][bulunan_nokta[0]].setFill(Color.WHITE);

							yon = yonDegistir(yon);
							if (adim_eksilt == true)
								i--;
							OyunEkrani.board[bulunan_nokta[1]][bulunan_nokta[0]].setFill(Color.GRAY);
							if(oyun_masasi.getMasa()[bulunan_nokta[1]][bulunan_nokta[0]]>0) {
								toplam_altin+=oyun_masasi.getMasa()[bulunan_nokta[1]][bulunan_nokta[0]];
								oyun_masasi.getMasa()[bulunan_nokta[1]][bulunan_nokta[0]]=0;
								toplam_altin+=oyun_masasi.getMasa()[bulunan_nokta[1]][bulunan_nokta[0]];

							}

						}
					}
				}
				if (hedef[1] == bulunan_nokta[1]) {
					yon = 1;

				}
			}

		}

		try {
			br.newLine();
		} catch (IOException e1) {
			System.out.println("B Oyuncusunda Dosyaya Yazma Hatasi");

		}

		if ((hedef[0] == bulunan_nokta[0]) && (hedef[1] == bulunan_nokta[1])) {
			toplam_altin += oyun_masasi.getMasa()[bulunan_nokta[1]][bulunan_nokta[0]];
			toplanan_altin_miktari += oyun_masasi.getMasa()[bulunan_nokta[1]][bulunan_nokta[0]];
			oyun_masasi.getMasa()[bulunan_nokta[1]][bulunan_nokta[0]] = 0;


			if (toplam_altin < hedef_belirleme_maliyeti) {
				eleniceDosyayaYaz();

				return 1;
			} else {
				hedefBelirle();
				yeniHedefiDosyayaYaz();
			}

			if ((hedef[0] == -1) && (hedef[1] == -1)) {
				return 2;
			} else {
				toplam_altin -= hedef_belirleme_maliyeti;
				harcanan_altin += hedef_belirleme_maliyeti;
			}
		}

		try {
			br.write("B Turunu Tamamladi. Sira Diger Oyuncuda");
			br.newLine();
			br.newLine();
		} catch (IOException e) {
			System.out.println("B Oyuncusunda Dosyaya Yazma Hatasi");
		}
		return 3;

	}

	/*
	 * Bu metot eger oyun sirasinda baska bir oyuncu bizim hedefimizi almis ise
	 * burada kontrol ediyoruz
	 */
	private Boolean hedefKontrolEt(int x, int y) {
		if (x == -1 && y == -1) {
			return false;
		}
		if (oyun_masasi.getMasa()[y][x] == 0) {
			hedef[0] = -1;
			hedef[1] = -1;
			return false;
		} else {
			return true;
		}
	}

	/*
	 * Eger ki oyuncu elenirse bunu dosyaya yazar
	 */
	private void eleniceDosyayaYaz() {

		if (!elendi_mi) {
			try {
				br.newLine();
				br.write("B Oyuncusu Altini Yeterli Olmadigi Icin Elenmistir");
				br.newLine();

			} catch (IOException e) {
				System.out.println("B Oyuncusunda Dosyaya Yazma Hatasi");
			}
		}

		elendi_mi = true;
	}

	/*
	 * Yeni Hedef Bulundugunda Bunu Dosyaya Yazar
	 */
	private void yeniHedefiDosyayaYaz() {
		try {
			if ((hedef[0] == -1) && (hedef[1] == -1)) {
				br.write("B Yeni Hedef Bulamadi. Turun Bitmesini Bekliyor...");
				br.newLine();
				br.newLine();
			} else {

				br.write("Yeni Hedef Bulundu  X:" + hedef[0] + " Y:" + hedef[1]);
				br.newLine();

			}

		} catch (IOException e) {
			System.out.println("B Oyuncusunda Dosyaya Yazma Hatasi");
		}

	}

	/*
	 * Gidilen her adimi dosyaya yazar. Gizli altin bulunursa bunu da uyari seklinde
	 * dosyaya ekler
	 */
	private void gidilenYoluYazdir(Boolean gizli_Mi, int yeni_x, int yeni_y) {
		try {
			if (gizli_Mi) {
				br.write("!!!!Gizli Altin Aciga Cikti(" + yeni_x + "," + yeni_y + ")!!!!--->");
			} else {
				br.write("(" + yeni_x + "," + yeni_y + ")--->");

			}
		} catch (IOException e) {
			System.out.println("B Oyuncusunda Dosyaya Yazma Hatasi");
		}
	}

	/*
	 * Hareket etmeden once bulunulan son konumu ve gidilecek hedefi dosyaya yazar
	 */
	private void hareketOncesiKonumuYazdir() {
		try {
			br.write("Son Konum: (" + bulunan_nokta[0] + "," + bulunan_nokta[1] + ")------->Hedef: (" + hedef[0] + ","
					+ hedef[1] + ")");
			br.newLine();
		} catch (IOException e) {
			System.out.println("B Oyuncusunda Dosyaya Yazma Hatasi");
		}
	}

	/*
	 * Oyunun en sonunda sonucu dosyaya yazdirmaya yarar.OyunMasas i classinda
	 * cagrilmasi onerilir
	 */
	public void sonuclariDosyayaYazdir() {
		try {
			br.newLine();
			br.newLine();
			br.write("----------------------------------");
			br.newLine();
			br.write("---------Oyun Sonu Durum----------");
			br.newLine();
			br.write("Kasada Bulunan Toplam Altin :" + toplam_altin);
			br.newLine();
			br.write("Atilan Toplam Adim        :" + atilan_adim_sayisi);
			br.newLine();
			br.write("Harcanan Altin Miktari    :" + harcanan_altin);
			br.newLine();
			br.write("Toplanan Altin Miktari    :" + toplanan_altin_miktari);
			br.newLine();
			br.write("----------------------------------");
		} catch (IOException e) {
			System.out.println("A Oyuncusunda Dosyaya Yazma Hatasi");

		}

	}

	public void oyunSonu() {
		sonuclariDosyayaYazdir();
		try {
			br.close();
		} catch (IOException e) {
			System.out.println("br kapatma Hatasi");
		}
	}

	public void oyunculariTanit(OyuncuA oyuncu_a, OyuncuC oyuncu_c, OyuncuD oyuncu_d) {
		this.oyuncu_a = oyuncu_a;
		this.oyuncu_c = oyuncu_c;
		this.oyuncu_d = oyuncu_d;
	}

	/*
	 * Eger false donerse o nokta da kimse yoktur
	 */
	public Boolean oyuncuOnumdeMi(int konum_x, int konum_y) {
		if (oyuncu_a.getBulunan_nokta()[0] == konum_x && oyuncu_a.getBulunan_nokta()[1] == konum_y) {
			return true;
		}
		if (oyuncu_c.getBulunan_nokta()[0] == konum_x && oyuncu_c.getBulunan_nokta()[1] == konum_y) {
			return true;
		}
		if (oyuncu_d.getBulunan_nokta()[0] == konum_x && oyuncu_d.getBulunan_nokta()[1] == konum_y) {
			return true;
		}

		return false;
	}

	/*
	 * Bu metot eger oyuncunun yoluna baska bir oyuncu cikarsa farkli bir yol tercih
	 * etmek icin kullaniliyor
	 */
	public int yonDegistir(int yon) {
		if ((yon == 1) && (bulunan_nokta[1] != hedef[1])) {
			return 2;
		} else if ((yon == 2) && (bulunan_nokta[0] != hedef[0])) {
			return 1;
		} else if (((yon == 1) && (bulunan_nokta[1] == hedef[1]))) {

			if (bulunan_nokta[1] - 1 >= 0) {
				bulunan_nokta[1] -= 1;

			} else {
				bulunan_nokta[1] += 1;
			}

			adim_eksilt = true;
			return 1;
		} else if ((yon == 2) && (bulunan_nokta[0] == hedef[0])) {

			if (bulunan_nokta[0] - 1 >= 0) {
				bulunan_nokta[0] -= 1;

			} else {
				bulunan_nokta[0] += 1;
			}

			adim_eksilt = true;
			return 2;
		}

		return 0;
	}

	public int getToplam_altin() {
		return toplam_altin;
	}

	public int getAtilan_adim_sayisi() {
		return atilan_adim_sayisi;
	}

	public int getHarcanan_altin() {
		return harcanan_altin;
	}

	public int getToplanan_altin_miktari() {
		return toplanan_altin_miktari;
	}

	public BufferedWriter getBr() {
		return br;
	}

	public int[] getBulunan_nokta() {
		return bulunan_nokta;
	}

	public int[] getHedef() {
		return hedef;
	}
}
