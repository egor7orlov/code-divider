import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;

public class PasteDivider extends AnAction {
    private static final int dividerLength = 65;

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        final Document document = editor.getDocument();
        Caret primaryCaret = editor.getCaretModel().getPrimaryCaret();
        int start = primaryCaret.getSelectionStart();
        int end = primaryCaret.getSelectionEnd();
        String selectedText = document.getText(new TextRange(start, end));

        WriteCommandAction.runWriteCommandAction(project, () ->
                document.replaceString(start, end, getDivider(selectedText))
        );

        primaryCaret.removeSelection();
    }

    public static String getDivider(String text) {
        String textToPaste = text.trim();

        if (textToPaste.length() <= dividerLength) {
            String initialDivider = "/* " + "-".repeat(dividerLength) + " */";
            int dividerHalfLength = initialDivider.length() / 2;
            int textHalfLength = textToPaste.length() / 2;
            String leftSide = initialDivider.substring(0, dividerHalfLength - textHalfLength);
            String rightSide = initialDivider.substring(dividerHalfLength + textHalfLength + (textToPaste.length() % 2));
            String output = leftSide + textToPaste + rightSide;

            return output;
        }

        return "/* (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧ Try shorter text */";
    }
}
