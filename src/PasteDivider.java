import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Objects;

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

        WriteCommandAction.runWriteCommandAction(project, () -> {
                    String selectedText = document.getText(new TextRange(start, end)).trim();

                    if (selectedText.length() <= dividerLength) {
                        document.replaceString(start, end, getDivider(selectedText));
                    } else {
                        JTextField textField = new JTextField("(ﾉ◕ヮ◕)ﾉ*:･ﾟ✧ Try shorter text", 20);
                        JComponent panel = new JPanel();

                        panel.add(textField);
                        JBPopupFactory.getInstance()
                                .createComponentPopupBuilder(panel, textField)
                                .createPopup()
                                .showInBestPositionFor(Objects.requireNonNull(e.getData(PlatformDataKeys.EDITOR)));
                    }
                }
        );

        primaryCaret.removeSelection();
    }

    public static String getDivider(String textToWrap) {
        String initialDivider = "/* " + "-".repeat(dividerLength) + " */";
        int dividerHalfLength = initialDivider.length() / 2;
        int textHalfLength = textToWrap.length() / 2;
        String leftSide = initialDivider.substring(0, dividerHalfLength - textHalfLength);
        String rightSide = initialDivider.substring(dividerHalfLength + textHalfLength + (textToWrap.length() % 2));

        return leftSide + textToWrap + rightSide;
    }
}
