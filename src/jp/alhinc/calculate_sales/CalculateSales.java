package jp.alhinc.calculate_sales;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalculateSales {

	// 支店定義ファイル名
	private static final String FILE_NAME_BRANCH_LST = "branch.lst";

	// 支店別集計ファイル名
	private static final String FILE_NAME_BRANCH_OUT = "branch.out";

	// エラーメッセージ
	private static final String UNKNOWN_ERROR = "予期せぬエラーが発生しました";
	private static final String FILE_NOT_EXIST = "支店定義ファイルが存在しません";
	private static final String FILE_INVALID_FORMAT = "支店定義ファイルのフォーマットが不正です";
	private static final String SALES_FILE_INVALID_FORMAT = "売上ファイル名が連番になっていません";
	private static final String SALE_AMOUNT_INVALID_FORMAT = "合計⾦額が10桁を超えました";
	private static final String BRANCH_CODE_INVALID_FORMAT = "の支店コードが不正です";
	private static final String INVALID_FORMAT = "のフォーマットが不正です";

	/**
	 * メインメソッド
	 *
	 * @param コマンドライン引数
	 */
	public static void main(String[] args) {
		// エラー処理3
		if (args.length != 1) {
			//コマンドライン引数が1つ設定されていなかった場合、エラーメッセージをコンソールに表⽰します。
			System.out.println("UNKNOWN_ERROR");
			return;
		}

		// 支店コードと支店名を保持するMap
		Map<String, String> branchNames = new HashMap<>();
		// 支店コードと売上金額を保持するMap
		Map<String, Long> branchSales = new HashMap<>();

		// 支店定義ファイル読み込み処理
		if (!readFile(args[0], FILE_NAME_BRANCH_LST, branchNames, branchSales)) {
			return;
		}

		// ※ここから集計処理を作成してください。(処理内容2-1、2-2)
		//listFilesを使用して、指定したパスに存在する全てのファイル(またはディレクトリ)の情報を配列filesに格納
		File[] files = new File(args[0]).listFiles();

		//先にファイルの情報を格納する List(ArrayList) を宣言
		List<File> rcdFiles = new ArrayList<>();

		// for文を使って指定したパスに存在する全てのファイル名を取得
		for (int i = 0; i < files.length; i++) {
			// 対象がファイルであり、「数字8桁.rcd」なのか判定し、一致したファイルをList<File>型の変数rcdFilesに格納
			if (files[i].isFile() && files[i].getName().matches("^[0-9]{8}[.]rcd$")) {
				rcdFiles.add(files[i]);
			}
		}

		// エラー処理2-1
		// 比較回数は売上ファイルの数よりも1回少ないため、繰り返し回数は売上ファイルのリストの数よりも1つ⼩さい数
		// クライアントがMacOSの場合を考慮して先にソートしておく
		Collections.sort(rcdFiles);

		for (int i = 0; i < rcdFiles.size() - 1; i++) {
			int former = Integer.parseInt(rcdFiles.get(i).getName().substring(0, 8));
			int latter = Integer.parseInt(rcdFiles.get(i + 1).getName().substring(0, 8));

			// 比較する2つのファイル名の先頭から数字の8⽂字を切り出し、int型に変換します。
			if ((latter - former) != 1) {
				// 2つのファイル名の数字に変換後、比較して差が1ではない場合、連番ではないため
				// エラーメッセージをコンソールに表示、処理を終了
				System.out.println(SALES_FILE_INVALID_FORMAT);
				return;
			}
		}

		BufferedReader br = null;
		// rcdFilesに複数の売上ファイルの情報を格納している為、その数だけ繰り返す
		for (int i = 0; i < rcdFiles.size(); i++) {

			// 支店定義ファイル読み込み(readFileメソッド)を参考に売上ファイルの中身を読み込む
			try {
				File file = rcdFiles.get(i);
				FileReader fr = new FileReader(file);
				br = new BufferedReader(fr);

				String line;
				List<String> FileData = new ArrayList<>();
				// 一行ずつ読み込む
				while ((line = br.readLine()) != null) {
					FileData.add(line);
					//売上ファイルの1行目には支店コード、2行目には売上金額が入っている
				}

				// エラー処理　ファイルのフォーマットチェック
				if (FileData.size() != 2) {
					//売上ファイルの⾏数が2⾏ではなかった場合、エラーメッセージをコンソールに表⽰
					System.out.println(file.getName() + INVALID_FORMAT);
					return;
				}

				// エラー処理　支店コードの有無チェック
				if (!branchSales.containsKey(FileData.get(0))) {
					//⽀店情報を保持しているMapに売上ファイルの⽀店コードが存在しなかった場合は、エラーメッセージをコンソールに表⽰
					System.out.println(file.getName() + BRANCH_CODE_INVALID_FORMAT);
					return;
				}

				// エラー処理3
				if (!FileData.get(1).matches("^[0-9]+$")) {
					//売上⾦額が数字ではなかった場合、エラーメッセージをコンソールに表⽰します。
					System.out.println(UNKNOWN_ERROR);
					return;
				}

				// 売上ファイルから読み込んだ売上金額をMapに加算していく為、型の変換を行う
				long fileSale = Long.parseLong(FileData.get(1));

				//読み込んだ売上⾦額を加算
				Long saleAmount = branchSales.get(FileData.get(0)) + fileSale;

				// エラー処理2
				if (saleAmount >= 10000000000L) {
					System.out.println(SALE_AMOUNT_INVALID_FORMAT);
					return;
				}

				//加算した売上⾦額をMapに追加
				branchSales.put(FileData.get(0), saleAmount);
			} catch (IOException e) {
				System.out.println(UNKNOWN_ERROR);
				return;
			} finally {
				// ファイルを開いている場合
				if (br != null) {
					try {
						// ファイルを閉じる
						br.close();
					} catch (IOException e) {
						System.out.println(UNKNOWN_ERROR);
						return;
					}
				}
			}
		}

		// 支店別集計ファイル書き込み処理
		if (!writeFile(args[0], FILE_NAME_BRANCH_OUT, branchNames, branchSales)) {
			return;
		}
	}

	/**
	 * 支店定義ファイル読み込み処理
	 *
	 * @param フォルダパス
	 * @param ファイル名
	 * @param 支店コードと支店名を保持するMap
	 * @param 支店コードと売上金額を保持するMap
	 * @return 読み込み可否
	 */
	private static boolean readFile(String path, String fileName, Map<String, String> branchNames,
			Map<String, Long> branchSales) {
		BufferedReader br = null;

		try {
			// 支店定義ファイルを開く
			File file = new File(path, fileName);
			// ファイルの存在チェック
			if (!file.exists()) {
				// 支店定義ファイルが存在しない場合、コンソールにエラーメッセージを表示、falseを返して処理を終了させる
				System.out.println(FILE_NOT_EXIST);
				return false;
			}
			// チェックを通過したら開いたファイルを読み込む
			FileReader fr = new FileReader(file);
			br = new BufferedReader(fr);

			String line;
			// 一行ずつ読み込む
			while ((line = br.readLine()) != null) {
				// ※ここの読み込み処理を変更してください。(処理内容1-2)
				String[] items = line.split(",");
				// ファイルのフォーマットチェック（↓チェック内容）
				// 仕様通りカンマで区切られていた場合は配列itemsに格納されている要素数は2つになるはず
				// 支店コードが3桁の数字で記載されているか
				// どちらか一方でも条件に該当していなければエラーメッセージを表示し、falseを返して処理を終了させる
				if (items.length != 2 || !items[0].matches("^[0-9]{3}$")) {
					System.out.println(FILE_INVALID_FORMAT);
					return false;
				}
				// チェックを通過したらMapに要素を追加
				branchNames.put(items[0], items[1]);
				branchSales.put(items[0], 0L);
			}

		} catch (IOException e) {
			System.out.println(UNKNOWN_ERROR);
			return false;
		} finally {
			// ファイルを開いている場合
			if (br != null) {
				try {
					// ファイルを閉じる
					br.close();
				} catch (IOException e) {
					System.out.println(UNKNOWN_ERROR);
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 支店別集計ファイル書き込み処理
	 *
	 * @param フォルダパス
	 * @param ファイル名
	 * @param 支店コードと支店名を保持するMap
	 * @param 支店コードと売上金額を保持するMap
	 * @return 書き込み可否
	 */
	private static boolean writeFile(String path, String fileName, Map<String, String> branchNames,
			Map<String, Long> branchSales) {
		// ※ここに書き込み処理を作成してください。(処理内容3-1)

		BufferedWriter bw = null;
		// ファイルを作成して書き込む
		try {
			File file = new File(path, fileName);
			FileWriter fw = new FileWriter(file);
			bw = new BufferedWriter(fw);

			// Mapから全てのKeyを取得
			for (String key : branchSales.keySet()) {
				// keyという変数には、Mapから取得したキーが代入される
				// 拡張for文で繰り返されている為、1つ目のキーが取得できたら、2つ目の取得
				// ...といったように、次々とkeyという変数に上書きされていく

				// writeメソッドで書き込んでいく
				bw.write(key + "," + branchNames.get(key) + "," + branchSales.get(key));
				// 改行
				bw.newLine();
			}
		} catch (IOException e) {
			System.out.println(UNKNOWN_ERROR);
			return false;
		} finally {
			// ファイルを開いている場合
			if (bw != null) {
				try {
					// ファイルを閉じる
					bw.close();
				} catch (IOException e) {
					System.out.println(UNKNOWN_ERROR);
					return false;
				}
			}
		}
		return true;
	}
}
