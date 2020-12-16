package backend;

import anamenu.OyunEkrani;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Formatter;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class OyunMasasi {
	private int x;
	private int y;

	private int altin_yuzdeligi;
	private int gizli_altin_yuzdeligi;

	private int[][] masa;

	private File cikti;
	private BufferedWriter br;

	private OyuncuA oyuncu_a;
	private OyuncuB oyuncu_b;
	private OyuncuC oyuncu_c;
	private OyuncuD oyuncu_d;

	private Random random = new Random();

	private Boolean oyuncu_durumlari[];

	public OyunMasasi(int x, int y, int altin_yuzdeligi, int gizli_altin_yuzdeligi) {

		this.x = x;
		this.y = y;
		this.altin_yuzdeligi = altin_yuzdeligi;
		this.gizli_altin_yuzdeligi = gizli_altin_yuzdeligi;

		masa = new int[y][x];
		masaOlustur();

		cikti = new File("OyunMasasi.txt");
		BufferedWriter br2;
		try {
			br2 = new BufferedWriter(new FileWriter(cikti));
			br = br2;
			br.write(
					"-------------------------------------------------------------------------------------------------------------");
			br.newLine();

		} catch (IOException e) {
			System.out.println("OyunMasasi Dosyaya Yazma hatasi");

		}
		tabloyuDosyayaYaz();

		try {
			br.newLine();
			br.write("Oyun Masasinda Altinlarin Baslangictaki Konumlari");
			br.newLine();
			br.write(
					"-------------------------------------------------------------------------------------------------------------");
			br.newLine();
			br.newLine();
			br.write("Masanin Baslangic Ayarlari:    ");
			br.newLine();
			br.newLine();
			br.write("Masanin Boyutu:  (X,Y): (" + x + "," + y + ")");
			br.newLine();
			br.write("Gorunur Altin Yuzdeligi: %" + altin_yuzdeligi);
			br.newLine();
			br.write("Gizli Altin Yuzdelil:    %" + gizli_altin_yuzdeligi);
			br.newLine();
			br.write("Masanin Koordinati X=yatay , Y=Dikey Duzlemleri Seklindedir");
			br.newLine();
			br.write("(0,0) Noktasi = Sol Ust Kose Olacak Sekilde Basliyor");
			br.newLine();
			br.newLine();
			br.write("NOT: Oyuncu ile ilgili ayarlar her oyuncunun kendi dosyasinda...");
			br.newLine();
			br.write(
					"-------------------------------------------------------------------------------------------------------------");
			br.newLine();

			br.newLine();
			br.newLine();
			br.newLine();
			br.write("\t\t\t\tOyun basliyor...");
			br.newLine();
			br.newLine();
			br.write("Konumlar (X,Y) Seklindedir");
			br.newLine();
		} catch (IOException e) {
			System.out.println("OyunMasasi Dosyaya Yazma hatasi");

		}

		/*
		 * 0.indis-A 1.indis-B 2.indis-C 3.indis-D
		 */
		oyuncu_durumlari = new Boolean[4];
		for (int i = 0; i < 4; i++) {
			oyuncu_durumlari[i] = true;
		}

	}

	private void masaOlustur() {

		int altin_sayisi = (x * y) * altin_yuzdeligi / 100;
		int gizli_altin_sayisi = altin_sayisi * gizli_altin_yuzdeligi / 100;

		int[] altin_satir_yerleri = new int[altin_sayisi - gizli_altin_sayisi];
		int[] altin_sutun_yerleri = new int[altin_sayisi - gizli_altin_sayisi];
		int[] altin_degerleri = new int[altin_sayisi - gizli_altin_sayisi];
		int[] gizli_altin_satir_yerleri = new int[gizli_altin_sayisi];
		int[] gizli_altin_sutun_yerleri = new int[gizli_altin_sayisi];
		int[] gizli_altin_degerleri = new int[gizli_altin_sayisi];

		/*
		 * Buradaki for dongusunde altinlarin hangi indislerde bulunacagi random bir
		 * sekilde dizilere kayit ediliyor
		 */
		for (int i = 0; i < altin_sayisi; i++) {
			int x_degeri = random.nextInt(x);
			int y_degeri = random.nextInt(y);
			Boolean kontrol = false;

			// Ayni alana tekrar altin koymamak icin kontrol ediyoruz
			for (int j = 0; j < altin_sayisi - gizli_altin_sayisi; j++) {

				if ((x_degeri == altin_satir_yerleri[j]) && (y_degeri == altin_sutun_yerleri[j])) {
					kontrol = true;
					break;
				}
			}

			for (int j = 0; j < gizli_altin_sayisi; j++) {

				if ((x_degeri == gizli_altin_satir_yerleri[j]) && (y_degeri == gizli_altin_sutun_yerleri[j])) {
					kontrol = true;
					break;
				}
			}

			if ((x_degeri == 0 && y_degeri == 0) || (x_degeri == x - 1 && y_degeri == 0)
					|| (x_degeri == 0 && y_degeri == y - 1) || (x_degeri == x - 1 && y_degeri == y - 1)) {
				kontrol = true;
			}

			if (kontrol) {
				i--;
				continue;
			}

			if (i < gizli_altin_sayisi) {
				gizli_altin_satir_yerleri[i] = x_degeri;
				gizli_altin_sutun_yerleri[i] = y_degeri;

			}

			else {
				altin_satir_yerleri[i - gizli_altin_sayisi] = x_degeri;
				altin_sutun_yerleri[i - gizli_altin_sayisi] = y_degeri;

			}

		}

		/*
		 * Buradaki for dongusunde ise hangi dizinde ne kadar altin bulunacaksa onu
		 * random bir sekilde atiyor
		 */

		for (int i = 0; i < altin_sayisi; i++) {
			if (i < gizli_altin_sayisi) {
				gizli_altin_degerleri[i] = (random.nextInt(4) + 1) * -5;

			}

			else {
				altin_degerleri[i - gizli_altin_sayisi] = (random.nextInt(4) + 1) * 5;

			}
		}

		/*
		 * Oyun masasini degerlerinin tutuldugu tabloya ilk degerler ataniyor
		 */
		for (int i = 0; i < y; i++) {
			for (int j = 0; j < x; j++) {
				masa[i][j] = 0;
			}
		}

		/*
		 * Oyun masasini degerlerinin tutuldugu tabloya altinlari ekliyoruz
		 */
		for (int i = 0; i < altin_sayisi; i++) {

			if (i < gizli_altin_sayisi) {
				masa[gizli_altin_sutun_yerleri[i]][gizli_altin_satir_yerleri[i]] = gizli_altin_degerleri[i];
				OyunEkrani.board[gizli_altin_sutun_yerleri[i]][gizli_altin_satir_yerleri[i]].setFill(Color.GREEN);
			}

			else {
				masa[altin_sutun_yerleri[i - gizli_altin_sayisi]][altin_satir_yerleri[i
						- gizli_altin_sayisi]] = altin_degerleri[i - gizli_altin_sayisi];
				OyunEkrani.board[altin_sutun_yerleri[i - gizli_altin_sayisi]][altin_satir_yerleri[i
						- gizli_altin_sayisi]].setFill(Color.YELLOW);
			}
		}

	}

	

	/*
	 * Bu Fonksiyonu calistirmayi unutma
	 */
	public void oyunculariAta(OyuncuA oyuncu_a, OyuncuB oyuncu_b, OyuncuC oyuncu_c, OyuncuD oyuncu_d) {
		this.oyuncu_a = oyuncu_a;
		this.oyuncu_b = oyuncu_b;
		this.oyuncu_c = oyuncu_c;
		this.oyuncu_d = oyuncu_d;

		oyuncu_a.oyunculariTanit(oyuncu_b, oyuncu_c, oyuncu_d);
		oyuncu_b.oyunculariTanit(oyuncu_a, oyuncu_c, oyuncu_d);
		oyuncu_c.oyunculariTanit(oyuncu_a, oyuncu_b, oyuncu_d);
	}

	/*
	 * Oyun basladi mi bitmesi icin 2 durum var Ya tum altinlar toplanmis olacak Ya
	 * da C nin erken elenmesi durumunda tum gorunur altinlar toplanacak
	 */
	public Boolean oynat(int sira) {
		try {
			if(sira==-1) {
				return true;
			}

			if (sira == 0) {
				oynaA();
			}
			if (sira == 1) {
				oynaB();

			}
			if (sira == 2) {
				oynaC();

			}
			if (sira == 3) {
				oynaD();

			}
			Boolean kontrol = false;
			for (int i = 0; i < 4; i++) {
				if (oyuncu_durumlari[i]) {
					kontrol = true;
				}
			}

			if (!kontrol) {
				br.newLine();
				br.newLine();
				br.write("!!!!Tum Oyuncularin Altini Bitmistir");
				br.newLine();
				br.write("!!!!Bu Yuzden Oyun Bitmistir");
				sonCiktiyiVer();
				return false;
			}

			if (tablo_bittiMi()) {
				br.newLine();
				br.newLine();
				br.write("!!!!Masada Altin Kalmadigi Icin Oyun Bitmistir");
				sonCiktiyiVer();
				return false;
			}

		} catch (IOException e) {
			System.out.println("Masa da oyuncularin ciktilarini verirken hata olusuyor.");
		}

		return true;
	}

	private void oynaA() {
		try {
			int son_durum;

			son_durum = 0;
			if (oyuncu_durumlari[0]) {
				br.write("A hareket Ediyor  (" + oyuncu_a.getBulunan_nokta()[0] + "," + oyuncu_a.getBulunan_nokta()[1]
						+ ")------>");
				son_durum = oyuncu_a.hareketEt();
				br.write("(" + oyuncu_a.getBulunan_nokta()[0] + "," + oyuncu_a.getBulunan_nokta()[1] + ")");
				br.newLine();
			}

			if (son_durum == 1) {
				br.write("!!!!!!!!!!!!!  A Oyuncusunun Altini Bitti A Oyuncusu Elendi");
				br.newLine();
				oyuncu_durumlari[0] = false;
			}

		} catch (IOException e) {
			System.out.println("Masa da A oyuncusu ciktilarini verirken hata olusuyor.");
		}
	}

	private void oynaB() {
		try {
			int son_durum;

			son_durum = 0;
			if (oyuncu_durumlari[1]) {
				br.write("B hareket Ediyor  (" + oyuncu_b.getBulunan_nokta()[0] + "," + oyuncu_b.getBulunan_nokta()[1]
						+ ")------>");

				son_durum = oyuncu_b.hareketEt();
				br.write("(" + oyuncu_b.getBulunan_nokta()[0] + "," + oyuncu_b.getBulunan_nokta()[1] + ")");
				br.newLine();
			}

			if (son_durum == 1) {
				br.write("!!!!!!!!!!!!!  B Oyuncusunun Altini Bitti B Oyuncusu Elendi");
				br.newLine();
				oyuncu_durumlari[1] = false;
			}

		} catch (IOException e) {
			System.out.println("Masa da B oyuncusu ciktilarini verirken hata olusuyor.");
		}
	}

	private void oynaC() {
		try {
			int son_durum;
			son_durum = 0;
			if (oyuncu_durumlari[2]) {
				br.write("C hareket Ediyor  (" + oyuncu_c.getBulunan_nokta()[0] + "," + oyuncu_c.getBulunan_nokta()[1]
						+ ")------>");

				son_durum = oyuncu_c.hareketEt();
				br.write("(" + oyuncu_c.getBulunan_nokta()[0] + "," + oyuncu_c.getBulunan_nokta()[1] + ")");
				br.newLine();
			}

			if (son_durum == 1) {
				br.write("!!!!!!!!!!!!!  C Oyuncusunun Altini Bitti C Oyuncusu Elendi");
				br.newLine();
				oyuncu_durumlari[2] = false;
			}

		} catch (IOException e) {
			System.out.println("Masa da C oyuncusu ciktilarini verirken hata olusuyor.");
		}
	}

	private void oynaD() {
		try {
			int son_durum;
			son_durum = 0;
			if (oyuncu_durumlari[3]) {
				br.write("D hareket Ediyor  (" + oyuncu_d.getBulunan_nokta()[0] + "," + oyuncu_c.getBulunan_nokta()[1]
						+ ")------>");

				son_durum = oyuncu_d.hareketEt();
				br.write("(" + oyuncu_d.getBulunan_nokta()[0] + "," + oyuncu_d.getBulunan_nokta()[1] + ")");
				br.newLine();
			}

			if (son_durum == 1) {
				br.write("!!!!!!!!!!!!!  D Oyuncusunun Altini Bitti C Oyuncusu Elendi");
				br.newLine();
				oyuncu_durumlari[3] = false;
			}

		} catch (IOException e) {
			System.out.println("Masa da D oyuncusu ciktilarini verirken hata olusuyor.");
		}
	}

	public int[][] getMasa() {
		return masa;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	/*
	 * Eger tabloda gorunur altin kalmamis ise false dondurur Eger ki gorunur altin
	 * hala varsa true dondurur
	 */
	public Boolean tablo_son_durum() {
		for (int i = 0; i < y; i++) {
			for (int j = 0; j < x; j++) {

				if (masa[i][j] > 0) {
					return true;
				}

			}

		}
		return false;
	}

	/*
	 * Tablodaki tum altinlarin toplanip toplanmadigini kontrol ediyor Eger
	 * toplanmis ise true Eger daha altin var ise false donuyor
	 * 
	 */
	public Boolean tablo_bittiMi() {
		for (int i = 0; i < y; i++) {
			for (int j = 0; j < x; j++) {

				if (masa[i][j] > 0) {
					return false;
				}

			}

		}
		return true;
	}

	/*
	 * Eger bir oyuncu gizli altinin ustunden gecerse altini aciga cikarmakan metod
	 */
	public Boolean gizliAltinOrtayaCikar(int gizli_x, int gizli_y) {
		if (masa[gizli_y][gizli_x] < 0) {
			if (gizli_y != -1 && gizli_x != -1) {
				OyunEkrani.board[gizli_y][gizli_x].setFill(Color.YELLOW);

				masa[gizli_y][gizli_x] *= -1;

				return true;
			}

		}

		return false;
	}

	/*
	 * Tablonun Matris Halini Dosyaya Yazmak icin Kullaniliyor
	 */
	public void tabloyuDosyayaYaz() {

		int altin_sayisi_kontrol = 0;

		try {

			for (int i = 0; i < y; i++) {

				for (int j = 0; j < x; j++) {
					Formatter f1 = new Formatter();
					f1.format("%-5d", masa[i][j]);
					br.write(f1.toString());
					if (masa[i][j] != 0)
						altin_sayisi_kontrol++;

					f1.close();
				}
				br.newLine();
			}

			br.write("Tablodaki Toplam Altin Sayisi: " + altin_sayisi_kontrol);
			br.newLine();
		} catch (IOException e) {
			System.out.println("OyunMasasi Dosyaya Matrisi Yazarken Hata Verdi");
		}

	}

	public void sonCiktiyiVer() {
		oyuncu_a.oyunSonu();
		oyuncu_b.oyunSonu();
		oyuncu_c.oyunSonu();
		oyuncu_d.oyunSonu();

		int kasadaki_toplam_altin_miktari = oyuncu_a.getToplam_altin() + oyuncu_b.getToplam_altin()
				+ oyuncu_c.getToplam_altin() + oyuncu_d.getToplam_altin();
		int toplam_harcanan_altin_miktari = oyuncu_a.getHarcanan_altin() + oyuncu_b.getHarcanan_altin()
				+ oyuncu_c.getHarcanan_altin() + oyuncu_d.getHarcanan_altin();
		int toplam_toplanan_altin_miktari = oyuncu_a.getToplanan_altin_miktari() + oyuncu_b.getToplanan_altin_miktari()
				+ oyuncu_c.getToplanan_altin_miktari() + oyuncu_d.getToplanan_altin_miktari();
		int toplam_atilan_adim_sayisi = oyuncu_a.getAtilan_adim_sayisi() + oyuncu_b.getAtilan_adim_sayisi()
				+ oyuncu_c.getAtilan_adim_sayisi() + oyuncu_d.getAtilan_adim_sayisi();
		try {
			br.newLine();
			br.write("--------------------------------------------------------------------------------");
			br.newLine();
			br.write("\t\t\tOyun Sonundaki Degerler");
			br.newLine();
			br.newLine();
			br.write("Oyuncularin:::");
			br.newLine();
			br.write("Kasalarindaki Toplam Altin Miktari: " + kasadaki_toplam_altin_miktari);
			br.newLine();
			br.write("Toplam Harcadiklari Altin: " + toplam_harcanan_altin_miktari);
			br.newLine();
			br.write("Toplam Topladiklari Altin: " + toplam_toplanan_altin_miktari);
			br.newLine();
			br.write("Toplam Atilan Adim Sayisi: " + toplam_atilan_adim_sayisi);
			br.newLine();
			br.newLine();
			br.write("--------------------------------------------------------------------------------");
			br.newLine();
			br.newLine();
			br.write("Masanin Son Hali");
			br.newLine();
			tabloyuDosyayaYaz();

			br.close();
		} catch (IOException e) {

			System.out.println("OyunMasasi.sonciktiyiVer  IOException Hatasi");
		}

		JFrame f =new JFrame();
		JOptionPane.showMessageDialog(f,"Oyun sonu. Lutfen cikti dosyalarini kontrol edin!","Uyari",JOptionPane.WARNING_MESSAGE);
		System.exit(0);
	}

}
