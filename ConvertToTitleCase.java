/**
 * Created by 44085037 on 16-Oct-17
 */
public class ConvertToTitleCase extends BaseProcessor {

    /**
     * Remove all occurrence of any digit in file name
     *
     * @param args Object of type Arguments
     * @return String modified file name
     */
    @Override
    protected String process(Arguments args) {
        /*String fileName = args.getFile().getName();
        if (Utils.hasValue(fileName)) {
            fileName = fileName.toLowerCase();
            for (int i = 0; i < fileName.length(); i++) {
                char ch = fileName.charAt(i);
                //if (ch == '-' || ch == ' ' || ch == '_')
                if (ch == ' ' || ch == '_') {
                    if (i != fileName.length() - 1) {
                        fileName = fileName.substring(0, i) + Utils.SPACE + Character.toString(fileName.charAt(i + 1)).toUpperCase() + fileName.substring(i + 2);
                    }
                    //} else if (ch == '(' || ch == '[')
                } else if (ch == '(') {
                    if (i != fileName.length() - 1) {
                        fileName = fileName.substring(0, i + 1) + Character.toString(fileName.charAt(i + 1)).toUpperCase() + fileName.substring(i + 2);
                    }
                }
            }
            fileName = fileName.replaceAll("_", Utils.EMPTY);
        }
        fileName = Character.toString(fileName.charAt(0)).toUpperCase() + fileName.substring(1);

        while (fileName.contains(Utils.DOUBLE_SPACE))
            fileName = fileName.replaceAll(Utils.DOUBLE_SPACE, Utils.SPACE);

        if (jcAppendFolder.isSelected()) {
            String folderName = getFolderName(args.getFile());
            // TODO: revisit
            // removing any numbers if present in folder name
            //folderName = removeNumbersFromFileNames(folderName.trim(), Utils.EMPTY, Utils.EMPTY, null);
//            folderName = removeSpacesFromBothSides(folderName.trim(), Utils.EMPTY, Utils.EMPTY, null);
            if (Utils.hasValue(folderName) && !fileName.toLowerCase().startsWith(folderName.toLowerCase() + Utils.SP_DASH_SP)) {
                if (fileName.toLowerCase().startsWith(folderName.toLowerCase()))
                    fileName = fileName.substring(0, folderName.length()) + Utils.SP_DASH_SP + fileName.substring(folderName.length());
                else
                    fileName = folderName + Utils.SP_DASH_SP + fileName;

                fileName = fileName.replaceAll(Utils.DOUBLE_SPACE, Utils.SPACE);
            }
        } else {
            String folderName = getFolderName(args.getFile());
            // TODO: revisit
            // removing any numbers if present in folder name
            //folderName = removeNumbersFromFileNames(folderName.trim(), Utils.EMPTY, Utils.EMPTY, null);
//            folderName = removeSpacesFromBothSides(folderName.trim(), Utils.EMPTY, Utils.EMPTY, null);
            if (Utils.hasValue(folderName) && fileName.toLowerCase().startsWith(folderName.toLowerCase())) {
                fileName = fileName.substring(folderName.length());
            }
            if (Utils.hasValue(folderName) && fileName.toLowerCase().startsWith(Utils.SP_DASH_SP)) {
                fileName = fileName.substring(Utils.SP_DASH_SP.length());
            }

            // trying same thing with non-modified folder name
            folderName = getFolderName(args.getFile(), false);
            // TODO: revisit
            // removing any numbers if present in folder name
            //folderName = removeNumbersFromFileNames(folderName.trim(), Utils.EMPTY, Utils.EMPTY, null);
            //folderName = removeSpacesFromBothSides(folderName.trim(), Utils.EMPTY, Utils.EMPTY, null);
            if (Utils.hasValue(folderName) && fileName.toLowerCase().startsWith(folderName.toLowerCase())) {
                fileName = fileName.substring(folderName.length());
            }
            if (Utils.hasValue(folderName) && fileName.toLowerCase().startsWith(Utils.SP_DASH_SP)) {
                fileName = fileName.substring(Utils.SP_DASH_SP.length());
            }
        }

        if (args.getFile().isFile())
            while (fileName.length() > FILENAME_LEN) {
                fileName = fileName.substring(0, FILENAME_LEN);
                if (fileName.contains(Utils.SPACE)) {
                    fileName = fileName.substring(0, fileName.lastIndexOf(Utils.SPACE));
                }
            }

//        fileName = removeSpacesFromBothSides(fileName, Utils.EMPTY, Utils.EMPTY, null);
        fileName = removeSpacesFromBothSides(args);
        return fileName;*/

        return null;
    }

}
