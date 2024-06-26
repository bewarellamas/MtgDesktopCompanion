package org.magic.gui.components.card;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.magic.api.beans.MTGEdition;
import org.magic.gui.abstracts.MTGUIComponent;
import org.magic.gui.components.widgets.JLangLabel;
import org.magic.services.MTGConstants;
import org.magic.services.tools.UITools;


public class MagicEditionDetailPanel extends MTGUIComponent {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private transient BindingGroup mBindingGroup;
	private MTGEdition magicEdition;
	private JTextField cardCountTextField;
	private JTextField releaseDateJTextField;
	private JTextField setJTextField;
	private JTextField typeJTextField;

	private JTextField blockJTextField;
	private JTextField idJtextField;
	private JCheckBox chkOnline;
	private JTextField cardOfficialCountTextField;
	private JTextField cardPhysicalCountTextField;

	public MagicEditionDetailPanel() {
		initGUI();
	}

	public void initGUI() {
		JPanel panneauHaut;

		panneauHaut = new JPanel();

		setLayout(new BorderLayout(0, 0));
		var gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 104, 333 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0 };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0 };
		panneauHaut.setLayout(gridBagLayout);

		panneauHaut.add(new JLangLabel("NAME",true), UITools.createGridBagConstraints(null, null, 0, 0));
		setJTextField = new JTextField();
		panneauHaut.add(setJTextField, UITools.createGridBagConstraints(null, GridBagConstraints.HORIZONTAL, 1, 0));

		panneauHaut.add(new JLangLabel("EDITION_TYPE",true), UITools.createGridBagConstraints(null, null, 0, 1));
		typeJTextField = new JTextField();
		panneauHaut.add(typeJTextField, UITools.createGridBagConstraints(null, GridBagConstraints.HORIZONTAL, 1, 1));

		panneauHaut.add(new JLangLabel("DATE_RELEASE",true), UITools.createGridBagConstraints(null, null, 0, 2));
		releaseDateJTextField = new JTextField();
		panneauHaut.add(releaseDateJTextField,  UITools.createGridBagConstraints(null, GridBagConstraints.HORIZONTAL, 1, 2));

		panneauHaut.add(new JLangLabel("EDITION_CARD_COUNT",true), UITools.createGridBagConstraints(null, null, 0, 4));
		
		cardCountTextField = new JTextField();
		cardOfficialCountTextField = new JTextField();
		cardPhysicalCountTextField = new JTextField();
		
		var pane = UITools.createFlowPanel(new JLabel("Total : "),cardCountTextField,new JLabel("Official :"),cardOfficialCountTextField,new JLabel("Physical:"),cardPhysicalCountTextField);
		
		panneauHaut.add(pane, UITools.createGridBagConstraints(null, GridBagConstraints.HORIZONTAL, 1, 4));

	
		panneauHaut.add(new JLangLabel("EDITION_BLOCK",true), UITools.createGridBagConstraints(null, null, 0, 5));
		blockJTextField = new JTextField(10);
		panneauHaut.add(blockJTextField, UITools.createGridBagConstraints(null, GridBagConstraints.HORIZONTAL, 1, 5));

		panneauHaut.add(new JLangLabel("ID",true), UITools.createGridBagConstraints(null, null, 0, 6));
		idJtextField = new JTextField(10);
		panneauHaut.add(idJtextField, UITools.createGridBagConstraints(null, GridBagConstraints.HORIZONTAL, 1, 6));

		panneauHaut.add(new JLangLabel("EDITION_ONLINE",true), UITools.createGridBagConstraints(null, null, 0, 7));
		chkOnline = new JCheckBox("");
		panneauHaut.add(chkOnline, UITools.createGridBagConstraints(GridBagConstraints.WEST,null, 1, 7));


		add(panneauHaut,BorderLayout.CENTER);
	
		if (magicEdition != null) {
			mBindingGroup = initDataBindings();
		}

		setEditable(false);
	}

	public void setEditable(boolean b) {
		idJtextField.setEditable(b);
		blockJTextField.setEditable(b);
		cardCountTextField.setEditable(b);
		cardOfficialCountTextField.setEditable(b);
		cardPhysicalCountTextField.setEditable(b);
		releaseDateJTextField.setEditable(b);
		typeJTextField.setEditable(b);
		setJTextField.setEditable(b);
		chkOnline.setEnabled(b);
	}

	public MTGEdition getMagicEdition() {
		return magicEdition;
	}

	public void init(MTGEdition newMagicEdition) {
		setMagicEdition(newMagicEdition, true);
	}

	public void setMagicEdition(MTGEdition newMagicEdition, boolean update) {
		magicEdition = newMagicEdition;


		if(isVisible() && update) {

				if (mBindingGroup != null) {
					mBindingGroup.unbind();
					mBindingGroup = null;
				}
				if (magicEdition != null) {
					mBindingGroup = initDataBindings();
				}
			}



	}

	@Override
	public void onVisible() {
		if (mBindingGroup != null) {
			mBindingGroup.unbind();
			mBindingGroup = null;
		}
		if (magicEdition != null) {
			mBindingGroup = initDataBindings();
		}
	}


	protected BindingGroup initDataBindings() {
		BeanProperty<MTGEdition, Integer> cardCountProperty = BeanProperty.create("cardCount");
		BeanProperty<JTextField, String> valueProperty = BeanProperty.create("text");
		AutoBinding<MTGEdition, Integer, JTextField, String> autoBinding3 = Bindings.createAutoBinding(
				UpdateStrategy.READ_WRITE, magicEdition, cardCountProperty, cardCountTextField, valueProperty);
		autoBinding3.bind();
		
		BeanProperty<MTGEdition, Integer> cardCountOfficialProperty = BeanProperty.create("cardCountOfficial");
		AutoBinding<MTGEdition, Integer, JTextField, String> autoBinding4 = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, magicEdition, cardCountOfficialProperty, cardOfficialCountTextField, valueProperty);
		autoBinding4.bind();
		
		BeanProperty<MTGEdition, Integer> cardCountPhysicialProperty = BeanProperty.create("cardCountPhysical");
		AutoBinding<MTGEdition, Integer, JTextField, String> autoBinding5 = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, magicEdition, cardCountPhysicialProperty, cardPhysicalCountTextField, valueProperty);
		autoBinding5.bind();
		
		
		//
		BeanProperty<MTGEdition, String> releaseDateProperty = BeanProperty.create("releaseDate");
		AutoBinding<MTGEdition, String, JTextField, String> autoBinding7 = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, magicEdition, releaseDateProperty, releaseDateJTextField, valueProperty);
		autoBinding7.bind();
		//
		BeanProperty<MTGEdition, String> setProperty = BeanProperty.create("set");
		AutoBinding<MTGEdition, String, JTextField, String> autoBinding8 = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, magicEdition, setProperty, setJTextField, valueProperty);
		autoBinding8.bind();
		//
		BeanProperty<MTGEdition, String> typeProperty = BeanProperty.create("type");
		AutoBinding<MTGEdition, String, JTextField, String> autoBinding11 = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, magicEdition, typeProperty, typeJTextField, valueProperty);
		autoBinding11.bind();

		BeanProperty<MTGEdition, String> blockProperty = BeanProperty.create("block");
		AutoBinding<MTGEdition, String, JTextField, String> autoBinding12 = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, magicEdition, blockProperty, blockJTextField, valueProperty);
		autoBinding12.bind();

		BeanProperty<MTGEdition, String> idProperty = BeanProperty.create("id");
		AutoBinding<MTGEdition, String, JTextField, String> autoBinding13 = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, magicEdition, idProperty, idJtextField, valueProperty);
		autoBinding13.bind();

		BeanProperty<MTGEdition, Boolean> onlineProperty = BeanProperty.create("onlineOnly");
		BeanProperty<JCheckBox, Boolean> chkProperty13 = BeanProperty.create("selected");
		AutoBinding<MTGEdition, Boolean, JCheckBox, Boolean> autoBinding14 = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, magicEdition, onlineProperty, chkOnline, chkProperty13);
		autoBinding14.bind();

		//
		var bindingGroup = new BindingGroup();
		//
		bindingGroup.addBinding(autoBinding3);
		bindingGroup.addBinding(autoBinding4);
		bindingGroup.addBinding(autoBinding5);
		bindingGroup.addBinding(autoBinding7);
		bindingGroup.addBinding(autoBinding8);
		bindingGroup.addBinding(autoBinding11);
		bindingGroup.addBinding(autoBinding12);
		bindingGroup.addBinding(autoBinding13);
		bindingGroup.addBinding(autoBinding14);
		return bindingGroup;
	}

	@Override
	public String getTitle() {
		return "Edition";
	}

	@Override
	public ImageIcon getIcon() {
		return MTGConstants.ICON_TAB_BACK;
	}




}
