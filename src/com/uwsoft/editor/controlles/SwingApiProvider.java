package com.uwsoft.editor.controlles;

import java.awt.Cursor;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

import com.badlogic.gdx.Gdx;
import com.uwsoft.editor.interfaces.IObserver;
import com.uwsoft.editor.renderer.data.ProjectInfoVO;
import com.uwsoft.editor.renderer.data.SceneVO;

public class SwingApiProvider implements IObserver {

    private CustomColorChooser colorPicker = new CustomColorChooser();

    @Override
    public String getObserverName() {
        return NameConstants.SWING_API;
    }

    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                NameConstants.SHOW_FILE_CHOOSER,
                NameConstants.PROJECT_OPENED,
                NameConstants.SET_CURSOR,
                NameConstants.SHOW_COLOR_PICKER,
                NameConstants.NEW_SCENE_CRATED,
                NameConstants.SCENE_DELETED};
    }

    @Override
    public void handleNotification(final String notificationName, final Object body) {
        Gdx.app.postRunnable(new Runnable() {

            @Override
            public void run() {
                switch (notificationName) {
                    case NameConstants.PROJECT_OPENED:
                        projectOpened();
                        initScenesMenu((ProjectInfoVO) body);
                        break;
                    case NameConstants.SET_CURSOR:
                        UIController.instance.frame.setCursor(new Cursor((int) body));
                        break;
                    case NameConstants.SHOW_FILE_CHOOSER:
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                FileChooserHandler handler = (FileChooserHandler) body;

                                JFileChooser jfc = new JFileChooser();
                                jfc.setMultiSelectionEnabled(handler.isMultiple());
                                jfc.setFileSelectionMode(handler.getFileSelectionMode());
                                jfc.showOpenDialog(UIController.instance.frame.getContentPane());
                                handler.FileChoosen(jfc);
                            }
                        });
                        break;
                    case NameConstants.SHOW_COLOR_PICKER:
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                ColorPickerHandler handler = (ColorPickerHandler) body;
                                colorPicker.setHandler(handler);
                                colorPicker.setVisible(true);
                            }
                        });
                        break;
                    case NameConstants.NEW_SCENE_CRATED:
                        SceneVO sceneVO = (SceneVO) body;
                        UIController.instance.menuTooolbar.addScene(sceneVO.sceneName);
                        break;
                    case NameConstants.SCENE_DELETED:
                        initScenesMenu((ProjectInfoVO) body);
                        break;
                }
            }
        });
    }

    private void initScenesMenu(ProjectInfoVO projectInfoVO) {
        UIController.instance.menuTooolbar.initScenesMenu();
        for (SceneVO sceneVO : projectInfoVO.scenes) {
            UIController.instance.menuTooolbar.addScene(sceneVO.sceneName);
        }
    }

    public void projectOpened() {
        UIController.instance.menuTooolbar.saveProjectBtn.setEnabled(true);
        UIController.instance.menuTooolbar.importBtn.setEnabled(true);
        UIController.instance.menuTooolbar.exportBtn.setEnabled(true);
        UIController.instance.menuTooolbar.exportSettingsBtn.setEnabled(true);
        UIController.instance.menuTooolbar.editItems.setEnabled(true);
        UIController.instance.menuTooolbar.sceneMenu.setEnabled(true);
    }

}