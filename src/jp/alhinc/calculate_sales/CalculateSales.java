package jp.alhinc.calculate_sales;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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

	/**
	 * メインメソッド
	 *
	 * @param コマンドライン引数
	 */
	public static void main(String[] args) {
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
			files[i].getName();
			// 取得したファイル名の中でファイル名が「数字8桁.rcd」なのか判定
			if (files[i].getName().matches("^[0-9]{8}.+rcd$")) {
				// 正規表現に一致したファイルをList<File>型の変数rcdFilesに格納
				rcdFiles.add(files[i]);
			}
		}

		BufferedReader br = null;
		//rcdFilesに複数の売上ファイルの情報を格納している為、その数だけ繰り返す
		for (int i = 0; i < rcdFiles.size(); i++) {

			//支店定義ファイル読み込み(readFileメソッド)を参考に売上ファイルの中身を読み込む
			try {
				File file = rcdFiles.get(i);
				FileReader fr = new FileReader(file);
				br = new BufferedReader(fr);

				String line;
				List<String> data = new ArrayList<>();
				// 一行ずつ読み込む
				while ((line = br.readLine()) != null) {
					data.add(line);
				}

			//売上ファイルの1行目には支店コード、2行目には売上金額が入っている

			//売上ファイルから読み込んだ売上金額をMapに加算していく為、型の変換を行う
			//※詳細は後述で説明
			long fileSale = Long.parseLong(売上金額);

			//読み込んだ売上⾦額を加算
			//※詳細は後述で説明
			Long saleAmount = 売上金額を入れたMap.get(⽀店コード) + long に変換した売上⾦額;

			//加算した売上⾦額をMapに追加

			} catch (IOException e) {

			} finally {

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
			File file = new File(path, fileName);
			FileReader fr = new FileReader(file);
			br = new BufferedReader(fr);

			String line;
			// 一行ずつ読み込む
			while ((line = br.readLine()) != null) {
				// ※ここの読み込み処理を変更してください。(処理内容1-2)
				String[] items = line.split(",");
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

		return true;
	}

}
